# -*- coding: utf-8 -*-

import sys
import xml.etree.ElementTree as XmlET
import json

import os
import re
import glob
import sqlite3
from zipfile import ZipFile


# =======================================================================
# Global
# =======================================================================


PUNCTUATIONS = (',', ';', '?', '!', '.', '，', '；', '？', '！', '。', ':', '：', '、')


# =======================================================================
# Poem class
# =======================================================================


class Poem:

    def __init__(self, title, subtitle, style, author, prologue, content, note, explanation):
        self.title = title
        self.subtitle = subtitle
        self.style = style
        self.author = author
        self.prologue = prologue
        self.content = content
        self.note = note
        self.explanation = explanation

    def equals(self, other):
        content1 = remove_punctuations(self.content)
        verses1 = content1.split('\n')

        content2 = remove_punctuations(other.content)
        verses2 = content2.split('\n')

        return verses1[0] == verses2[0] # and verses1[1] == verses2[1]

    def get_tuple(self):
        return (self.title.encode('utf_16_le'),
                self.subtitle.encode('utf_16_le'),
                self.style.encode('utf_16_le'),
                self.author.encode('utf_16_le'),
                self.prologue.encode('utf_16_le'),
                self.content.encode('utf_16_le'),
                self.note.encode('utf_16_le'),
                self.explanation.encode('utf_16_le'))

    def get_json(self):
        return json.dumps(self, default=lambda o: o.__dict__, ensure_ascii=False, sort_keys=True, indent=4)


def poem_dict(poem):
    return poem.__dict__


def remove_punctuations(content):
    text = content
    for c in PUNCTUATIONS:
        text = text.replace(c, '')
    return text


def poem_content_to_fragment_list(content):
    text = content
    for c in PUNCTUATIONS:
        text = text.replace(c, '')
    return text.split('\n')


def write_poems_json_file(jsonfile, poems):
    with open(jsonfile, 'w') as fh:
        fh.write(json.dumps(poems, default=poem_dict, ensure_ascii=False, sort_keys=True, indent=4))


def write_poems_text_file(txtfile, poems):
    with open(txtfile, 'w') as fh:
        for poem in poems:
            content = poem.content
            for c in PUNCTUATIONS:
                content = content.replace(c, '')
            fh.write(content)


def load_formatted_poem_json(jsonfile):
    poems = []
    with open(jsonfile) as fh:
        data = json.load(fh)
        for p in data:
            poem = Poem(p['title'], p['subtitle'], p['style'], p['author'], p['prologue'], p['content'],
                        p['note'], p['explanation'])
            poems.append(poem)

    print(str(len(poems)) + " poems were loaded.")

    # return poem list
    return poems


# =======================================================================
# paring TangShi300 xml file
# =======================================================================


def parse_300_tang_poems(xmlfile):

    print("Parsing " + xmlfile + "...")

    # create element tree object
    tree = XmlET.parse(xmlfile)

    # get root element
    root = tree.getroot().find("lyrics")

    # create empty list for poems
    poems = []

    # all lyric items
    items = root.findall('lyric')

    # iterate poem items
    for item in items:

        # init fields
        title = ''
        subtitle = ''
        author = ''
        prologue = ''
        style = ''
        content = ''
        explanation = ''
        comment = ''
        note = ''

        # iterate child elements of item
        for child in item:

            # get filed values of a poem
            if child.text:
                if child.tag == 'name':
                    title = child.text.encode('utf8')
                elif child.tag == 'sname':
                    subtitle = child.text.encode('utf8')
                elif child.tag == 'author':
                    author = child.text.encode('utf8')
                elif child.tag == 'prologue':
                    prologue = child.text.encode('utf8')
                elif child.tag == 'type':
                    style = child.text.encode('utf8')
                elif child.tag == 'content':
                    content = child.text
                    if not content.endswith('\n'):
                        content = content + '\n'
                    content = content.encode('utf8')
                elif child.tag == 'meaning':
                    explanation = child.text.encode('utf8')
                elif child.tag == 'comment':
                    comment = child.text.encode('utf8')
                elif child.tag == 'word':
                    note = child.text.encode('utf8')

        # build the poem
        poem = Poem(title, subtitle, style, author, prologue, content, comment + '\n' + note, explanation)
        # append the poem to poem list
        poems.append(poem)

    print(str(len(poems)) + " poems were parsed.")

    # return poem list
    return poems


# =======================================================================
# paring QianJiaShi text file
# =======================================================================


POEM_STYLES = ('五言律诗', '五言绝句', '七言律诗', '七言绝句', '五言古诗', '五言乐府', '七言古诗', '七言乐府')
POEM_STYLE_LINE_COUNT = {'五言律诗': 4, '五言绝句': 2, '七言律诗': 4, '七言绝句': 2}
POEM_STYLES_REF = ('五律-', '五绝-', '七律-', '七绝-')
MIN_AUTHOR_INTRODUCTION_LEN = 48


def is_poem_style_line(line):
    for style in POEM_STYLES_REF:
        if style in line:
            return POEM_STYLES_REF.index(style)

    return -1


def format_content(text_list):
    text = ''
    for txt in text_list:
        lines = txt.split('，')
        if len(lines) != 2:
            lines = txt.split('？')
            if len(lines) != 2:
                lines = txt.split(',')
                if len(lines) != 2:
                    lines = txt.split('?')
        try:
            text += lines[0] + '，\n'
            text += lines[1] + '\n'
        except Exception as e:
            print("Failed to format content: " + e.message)

    text = text.replace('\n\n', '\n')
    l = len(text)
    if text[l-1] == '\n':
        text = text[:l-1]

    return text


def parse_poems_of_1000_writers(txtfile):

    print("Parsing " + txtfile + "...")

    # create empty list for poems
    poems = []

    # read file and parse line by line
    with open(txtfile, "r") as fh:

        # init fields
        title = ''
        subtitle = ''
        author = ''
        prologue = ''
        style = ''
        content = []
        explanation = ''
        note = ''
        author_introduction = ''

        # parse line by line
        for line in fh:

            # remove spaces
            line = line.strip()

            # empty line
            if not line:
                continue

            line = line.encode('utf-8')

            # style
            idx = is_poem_style_line(line)
            if idx != -1:
                style = POEM_STYLES[idx]
                continue

            # if content filled, then the poem is ready
            if len(content) == POEM_STYLE_LINE_COUNT[style]:

                # note
                if not note and '。' in line:
                    note = line

                # end of one poem, build the poem object and add to list
                poem = Poem(title, subtitle, style, author, prologue, format_content(content), note, explanation)
                poems.append(poem)

                # init fields for next poem
                title = ''
                subtitle = ''
                author = ''
                prologue = ''
                content = []
                explanation = ''
                author_introduction = ''

                # if note filled, current has been used, go for next line
                if note:
                    note = ''
                    continue

                note = ''

                # otherwise, current line should be title for next poem

            # title
            if not title:
                title = line
                continue

            # author
            if not author:
                author = line
                continue

            # author introduction
            if not author_introduction and (len(line) > MIN_AUTHOR_INTRODUCTION_LEN or author in line or line.count('，') > 1):
                author_introduction = line
                continue

            # content
            if len(content) < POEM_STYLE_LINE_COUNT[style]:
                content.append(line)
                continue

    # possible last poem
    if title and author and len(content) > 0:
        poem = Poem(title, subtitle, style, author, prologue, format_content(content), note, explanation)
        poems.append(poem)

    print(str(len(poems)) + " poems were parsed.")

    # return poem list
    return poems


# =======================================================================
# paring SongCi300 text file
# =======================================================================


AUTHOR_TITLE_MIN_LEN = 32
AUTHOR_TITLE_END1 = '首)'
AUTHOR_TITLE_END2 = '首）'
NOTE_TITLE = '【注释】'


def not_sentence(line):
    for c in PUNCTUATIONS:
        if c in line:
            return False

    return True


def is_author_title(line):
    return not_sentence(line) and AUTHOR_TITLE_END1 in line or AUTHOR_TITLE_END2 in line


def is_note_title(line):
    return not_sentence(line) and NOTE_TITLE in line


def is_author(line, author_title):
    return line in author_title


def is_title(line, author_title):
    return not_sentence(line) and not is_author(line, author_title)


def format_ci_content(text):
    text = text.replace(' ', '')
    text = text.replace('', '')
    text = text.replace('', '')
    punc = PUNCTUATIONS[:len(PUNCTUATIONS)-1]
    for c in punc:
        text = text.replace(c, c + '\n')
    text = text.replace('\n\n', '\n')
    l = len(text)
    if text[l-1] == '\n':
        text = text[:l-1]
    return text


def format_ci_note(text):
    text = text.replace('［', '\n［')
    text = text.replace('\n\n', '\n')
    if text[0] == '\n':
        text = text[1:]
    return text


def poem_ready(title, author, content):
    return title and author and content


def parse_300_song_ci(txtfile):

    print("Parsing " + txtfile + "...")

    # create empty list for poems
    poems = []

    # read file and parse line by line
    with open(txtfile, "r") as fh:

        # init fields
        title = ''
        subtitle = ''
        author = ''
        prologue = ''
        style = '词'
        content = ''
        explanation = ''
        note = ''

        author_introduction = ''
        author_title = ''

        author_title_started = False
        title_set = False
        note_started = False

        # parse line by line
        for line in fh:

            # remove spaces
            line = line.strip()

            # empty line
            if not line:
                continue

            line = line.encode('utf-8')

            # author title
            if is_author_title(line):

                # a poem was processed before
                if poem_ready(title, author, content):
                    # end of one poem, build the poem object and add to list
                    poem = Poem(title, subtitle, style, author, prologue, format_ci_content(content), format_ci_note(note), explanation)
                    poems.append(poem)

                    # init fields for next poem
                    title = ''
                    subtitle = ''
                    author = ''
                    prologue = ''
                    # style = ''
                    content = ''
                    explanation = ''
                    note = ''

                note_started = False

                author_title = line
                author_title_started = True
                continue

            # author introduction
            if author_title_started:
                # title
                if is_title(line, author_title):

                    if poem_ready(title, author, content):
                        # end of one poem, build the poem object and add to list
                        poem = Poem(title, subtitle, style, author, prologue, format_ci_content(content), format_ci_note(note), explanation)
                        poems.append(poem)

                        # init fields for next poem
                        # title = ''
                        subtitle = ''
                        author = ''
                        prologue = ''
                        # style = ''
                        content = ''
                        explanation = ''
                        note = ''

                    note_started = False

                    title = line
                    title_set = True
                    author_title_started = False
                    continue

                if author_introduction:
                    author_introduction += '\n'
                author_introduction += line
                continue

            # note started
            if is_note_title(line):
                note_started = True
                continue

            # title
            if is_title(line, author_title) and not title_set:

                if poem_ready(title, author, content):
                    # end of one poem, build the poem object and add to list
                    poem = Poem(title, subtitle, style, author, prologue, format_ci_content(content), format_ci_note(note), explanation)
                    poems.append(poem)

                    # init fields for next poem
                    # title = ''
                    subtitle = ''
                    author = ''
                    prologue = ''
                    # style = ''
                    content = ''
                    explanation = ''
                    note = ''

                note_started = False

                title = line
                title_set = True
                continue

            # subtitle
            if is_title(line, author_title) and title_set and not note_started:
                subtitle = line
                title_set = False
                continue

            # author
            if not author and is_author(line, author_title) and not note_started:
                author = line
                title_set = False
                continue

            # content
            if not note_started:
                content += line
                continue

            # note
            if note_started:
                if note:
                    note += '\n'
                note += line
                continue

    # last poem
    if poem_ready(title, author, content):
        poem = Poem(title, subtitle, style, author, prologue, format_ci_content(content), format_ci_note(note), explanation)
        poems.append(poem)

    print(str(len(poems)) + " poems were parsed.")

    # return poem list
    return poems


# =======================================================================
# paring GuShiCi500 text file
# =======================================================================


LEVEL_START = '第'
LEVEL_END = '级'
ORDER_END = '**'
TITLE_START = '《'
TITLE_END = '》'
TITLE_START_1 = '【'
TITLE_END_1 = '】'


def is_level_line(line):
    return line.startswith(LEVEL_START) and line.endswith(LEVEL_END)


def is_order_line(line):
    return line[:1].isdigit() and line.endswith(ORDER_END)


def get_author_and_title(line):
    author = ''
    title = ''
    is_qu = False

    i = line.find(TITLE_START)
    if i == -1:
        i = line.find(TITLE_START_1)

    j = line.find(TITLE_END)
    if j == -1:
        j = line.find(TITLE_END_1)
        is_qu = True

    if i != -1 and j != -1 and i < j:
        author = line[:i].replace(' ', '')
        title = line[i+3:j].replace(' ', '')

        if is_qu:
            title_ex = line[j+3:].replace('　', ' ')
            while title_ex.find('  ') != -1:
                title_ex = title_ex.replace('  ', ' ')
            title_ex = title_ex.replace(' ', '·')
            title += '·' + title_ex

    return author, title


def parse_500_gu_shi_ci(txtfile):

    print("Parsing " + txtfile + "...")

    # create empty list for poems
    poems = []

    # read file and parse line by line
    with open(txtfile, "r") as fh:

        # init fields
        title = ''
        subtitle = ''
        author = ''
        prologue = ''
        style = ''
        content = ''
        explanation = ''
        note = ''

        # level count
        lc = 0
        # list of poem count in each level
        lpc = []

        # parse line by line
        for line in fh:

            # remove spaces
            line = line.strip()

            # empty line
            if not line:
                continue

            line = line.encode('utf-8')

            is_ll = is_level_line(line)
            if is_ll or is_order_line(line):

                # end passed poem
                if title and content:
                    # end of one poem, build the poem object and add to list
                    poem = Poem(title, subtitle, style, author, prologue, format_ci_content(content), note, explanation)
                    poems.append(poem)

                    # init fields for next poem
                    title = ''
                    subtitle = ''
                    author = ''
                    prologue = ''
                    style = ''
                    content = ''
                    explanation = ''
                    note = ''

                    # increase current level count
                    lpc[lc-1] += 1

                # level count
                if is_ll:
                    lpc.append(0)
                    lc += 1

                continue

            # author and title
            if not author and not title:
                author, title = get_author_and_title(line)
                continue

            # content
            content += line

        # last poem
        if title and content:
            # end of one poem, build the poem object and add to list
            poem = Poem(title, subtitle, style, author, prologue, format_ci_content(content), note, explanation)
            poems.append(poem)

            lpc[lc - 1] += 1

    print(str(len(poems)) + " poems were parsed.")

    # return poem list
    return poems


# =======================================================================
# paring ZhongXiaoXue GuShiCi 116 text file
# =======================================================================


def is_title_line(line):
    i = line.find(TITLE_START)
    j = line.find(TITLE_END)
    return i != -1 and j != -1 and i < j


def get_title(line):
    title = ''
    subtitle = ''

    i = line.find(TITLE_START)
    j = line.find(TITLE_END)
    if i != -1 and j != -1 and i < j:
        title = line[i+3: j]
        subtitle = line[j+3:]

    return title, subtitle


def get_author(line):
    i = line.find('·')
    if i == -1:
        return line
    return line[i+2:]


def parse_zhongxiaoxue_gu_shi_ci_116(txtfile):

    print("Parsing " + txtfile + "...")

    # create empty list for poems
    poems = []

    # read file and parse line by line
    with open(txtfile, "r") as fh:

        # init fields
        title = ''
        subtitle = ''
        author = ''
        prologue = ''
        style = ''
        content = ''
        explanation = ''
        note = ''

        # parse line by line
        for line in fh:

            # remove spaces
            line = line.strip()

            # empty line
            if not line:
                continue

            line = line.encode('utf-8')

            if is_title_line(line):

                # end passed poem
                if title and content:
                    # end of one poem, build the poem object and add to list
                    poem = Poem(title, subtitle, style, author, prologue, format_ci_content(content), note, explanation)
                    poems.append(poem)

                    # init fields for next poem
                    title = ''
                    subtitle = ''
                    author = ''
                    prologue = ''
                    style = ''
                    content = ''
                    explanation = ''
                    note = ''

                title, subtitle = get_title(line)
                continue

            # author and title
            if not author:
                author = get_author(line)
                continue

            # content
            content += line

        # last poem
        if title and content:
            # end of one poem, build the poem object and add to list
            poem = Poem(title, subtitle, style, author, prologue, format_ci_content(content), note, explanation)
            poems.append(poem)

    print(str(len(poems)) + " poems were parsed.")

    # return poem list
    return poems


# =======================================================================
# paring ZhongXiaoXue GuShiCi 75 text file
# =======================================================================


TITLE_LINE_MARK = '、'


def is_title_line_75(line):
    i = line.find(TITLE_LINE_MARK)
    return i != -1 and i <= 2


def get_title_75(line):
    title = ''
    subtitle = ''

    i = line.find(TITLE_LINE_MARK)
    if i != -1 and i <= 2:
        titles = line[i+3:].split(' ')
        title = titles[0]
        if len(titles) > 1:
            subtitle = titles[1]

    return title, subtitle


def parse_zhongxiaoxue_gu_shi_ci_75(txtfile):

    print("Parsing " + txtfile + "...")

    # create empty list for poems
    poems = []

    # read file and parse line by line
    with open(txtfile, "r") as fh:

        # init fields
        title = ''
        subtitle = ''
        author = ''
        prologue = ''
        style = ''
        content = ''
        explanation = ''
        note = ''

        # parse line by line
        for line in fh:

            # remove spaces
            line = line.strip()

            # empty line
            if not line:
                continue

            line = line.encode('utf-8')

            if is_title_line_75(line):

                # end passed poem
                if title and content:
                    # end of one poem, build the poem object and add to list
                    poem = Poem(title, subtitle, style, author, prologue, format_ci_content(content), note, explanation)
                    poems.append(poem)

                    # init fields for next poem
                    title = ''
                    subtitle = ''
                    author = ''
                    prologue = ''
                    style = ''
                    content = ''
                    explanation = ''
                    note = ''

                title, subtitle = get_title_75(line)
                continue

            # author and title
            if not author:
                author = get_author(line)
                continue

            # content
            content += line

        # last poem
        if title and content:
            # end of one poem, build the poem object and add to list
            poem = Poem(title, subtitle, style, author, prologue, format_ci_content(content), note, explanation)
            poems.append(poem)

    print(str(len(poems)) + " poems were parsed.")

    # return poem list
    return poems


# =======================================================================
# loading SongCi300 json file
# =======================================================================
def load_300_song_ci_json(jsonfile):
    poems = []
    with open(jsonfile) as fh:
        data = json.load(fh)
        for p in data:
            content = ''
            for line in p['content']:
                content += line
            poem = Poem(p['title'], '', '词', p['author'], '', format_ci_content(content), '', '')
            poems.append(poem)

    print(str(len(poems)) + " poems were loaded.")

    # return poem list
    return poems


# =======================================================================
# build poem fragment library in text file from formatted json file
# =======================================================================
def build_poem_fragment_lib_from_formatted_json(jsonfile, txtfile):
    fragments = []

    poems = load_formatted_poem_json(jsonfile)
    for p in poems:
        fl = poem_content_to_fragment_list(p.content)
        fragments.extend(fl)

    with open(txtfile, 'w') as fh:
        for f in fragments:
            fh.write(f + '\n')

    print(str(len(fragments)) + " poem fragments were written." + txtfile)


# =======================================================================
# build poem fragment library in text file
# =======================================================================
def build_poem_fragment_lib(txtfile):
    fragments = []
    duplicated1 = 0
    duplicated2 = 0

    poems = load_formatted_poem_json('data/result/qianjiashi.json')
    for p in poems:
        fl = poem_content_to_fragment_list(p.content)
        fragments.extend(fl)

    poems = load_formatted_poem_json('data/result/tangshi300.json')
    for p in poems:
        fl = poem_content_to_fragment_list(p.content)
        for f in fl:
            if f not in fragments:
                fragments.append(f)
            else:
                duplicated1 = duplicated1 + 1

    poems = load_formatted_poem_json('data/result/songci300-1.json')
    for p in poems:
        fl = poem_content_to_fragment_list(p.content)
        for f in fl:
            if f not in fragments:
                fragments.append(f)
            else:
                duplicated2 = duplicated2 + 1

    with open(txtfile, 'w') as fh:
        for f in fragments:
            fh.write(f + '\n')

    print(str(len(fragments)) + " poem fragments were written to " + txtfile)


# =======================================================================
# main()
# =======================================================================


def main():
    reload(sys)
    sys.setdefaultencoding("utf-8")

    poems = parse_zhongxiaoxue_gu_shi_ci_75('data/中小学古诗词75.txt')
    write_poems_json_file('data/result/zongxiaoxue_gushici_75.json', poems)

    poems1 = parse_zhongxiaoxue_gu_shi_ci_116('data/中小学古诗词116.txt')
    write_poems_json_file('data/result/zongxiaoxue_gushici_116.json', poems1)

    # build complete zhong xiao xue gu shi ci
    print('\nbuilding zongxiaoxue_gushici_complete.json')
    # diff zhong xiao xue gu shi ci 75 with 116
    count = 0
    for p in poems:
        found = False
        for p1 in poems1:
            if p.equals(p1):
                found = True
        if not found:
            poems1.append(p)
            count += 1
    print(str(len(poems1)) + ' poems were merged. ' + str(count) + ' poems were added.')
    write_poems_json_file('data/result/zongxiaoxue_gushici_complete.json', poems1)

    poems = parse_500_gu_shi_ci('data/古诗词五百首.txt')
    write_poems_json_file('data/result/gushici500.json', poems)

    # build complete zhong xiao xue gu shi ci
    print('\nbuilding gushici500_complete.json')
    # diff gushici500 with zhong xiao xue gu shi ci
    count = 0;
    for p1 in poems1:
        found = False
        for p in poems:
            if p.equals(p1):
                found = True
        if not found:
            poems.append(p1)
            count += 1
    print(str(len(poems)) + ' poems were merged from zhongxiaoxue gushici. ' + str(count) + ' poems were added.')
    # write_poems_json_file('data/result/gushici500_complete.json', poems)


    poems1 = parse_poems_of_1000_writers('data/千家诗.txt')
    write_poems_json_file('data/result/qianjiashi.json', poems1)
    # write_poems_text_file('data/result/shiju.txt', poems)

    # merge qianjiashi into gushici 600
    qianjiashi_to_be_merged_into_gushici_600 = (
    '春日偶成,程颢',
    '春宵,苏轼',
    '海棠,苏轼',
    '赠刘景文,苏轼',
    '绝句,僧志南',
    '书湖阴先生壁,王安石',
    '乌衣巷,刘禹锡',
    '霜月,李商隐',
    '雪梅 其一,卢梅坡',
    '寓意,晏殊',
    '秋风引,刘禹锡',
    '秋日湖上,薛莹',
    '秋登宣城谢北楼,李白',
    '春夜,王安石',
    '客中行,李白')
    count = 0;
    for p1 in poems1:
        pid1 = p1.title + ',' + p1.author
        for pid in qianjiashi_to_be_merged_into_gushici_600:
            if pid == pid1:
                poems.append(p1)
                count += 1
    print(str(len(poems)) + ' poems were merged from qinajiashi. ' + str(count) + ' poems were added.')
    write_poems_json_file('data/result/gushici600.json', poems)



    #
    # poems = parse_300_tang_poems('data/唐诗三百首.xml')
    # write_poems_json_file('data/result/tangshi300.json', poems)

    # poems = parse_300_song_ci('data/宋词三百首.txt')
    # write_poems_json_file('data/result/songci300.json', poems)
    # # write_poems_text_file('data/result/shiju.txt', poems)

    # poems = load_300_song_ci_json('data/宋词三百首.json')
    # write_poems_json_file('data/result/songci300-1.json', poems)
    # # write_poems_text_file('data/result/shiju.txt', poems)

    # build_poem_fragment_lib('data/result/cf_lib_tangshi300_songci300.txt')

    build_poem_fragment_lib_from_formatted_json('data/result/zongxiaoxue_gushici_complete.json', 'data/result/cf_lib_zhongxiaoxue_gushici.txt')
    build_poem_fragment_lib_from_formatted_json('data/result/gushici600.json', 'data/result/cf_lib_gushici600.txt')


if __name__ == "__main__":
    main()