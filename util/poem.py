# -*- coding: utf-8 -*-

import sys
import json


PUNCTUATIONS = (',', ';', '?', '!', '.', '，', '；', '？', '！', '。', ':', '：', '、')


# =======================================================================
# Poem class
# =======================================================================


class Poem:

    def __init__(self, title, subtitle, style, author, prologue, content, period, note, explanation):
        self.title = title
        self.subtitle = subtitle
        self.style = style
        self.author = author
        self.prologue = prologue
        self.content = content
        self.period = period
        self.note = note
        self.explanation = explanation
        self.level = 0

    def set_level(self, level):
        self.level = level

    def equals(self, other):
        first1, second1 = get_first_second(self.content)
        first2, second2 = get_first_second(other.content)

        return first1 == first2 or second1 == second2

    def to_string(self):
        s = self.title + '  ' + self.subtitle + '\n'

        if self.period:
            s += '(' + self.period + ') '
        s += self.author + '\n'

        s += self.content

        return s

    def get_json(self):
        return json.dumps(self, default=lambda o: o.__dict__, ensure_ascii=False, sort_keys=True, indent=4)

    def display(self):
        print(self.to_string())


def poem_dict(poem):
    return poem.__dict__


# =======================================================================
# Poem util methods
# =======================================================================

def break_combined_sentences(txt):
    lines = txt.split('，')
    if len(lines) < 2:
        lines = txt.split('？')
        if len(lines) < 2:
            lines = txt.split(',')
            if len(lines) < 2:
                lines = txt.split('?')
    first = lines[0]
    second = ''
    if len(lines) >= 2:
        second = lines[1]

    return first, second


def get_first_second(content):
    verses = content.split('\n')
    i = 0
    first, second = break_combined_sentences(verses[i])
    i += 1

    while not first:
        first, second = break_combined_sentences(verses[i])
        i += 1

    while not second:
        second, third = break_combined_sentences(verses[i])
        i += 1

    return remove_punctuations(first), remove_punctuations(second)

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
            poem = Poem(p['title'], p['subtitle'], p['style'], p['author'], p['prologue'], p['content'], p['period'],
                        p['note'], p['explanation'])
            poem.set_level(p['level'])
            poems.append(poem)

    print(str(len(poems)) + " poems were loaded.")

    # return poem list
    return poems

