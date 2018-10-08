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
        content1 = remove_punctuations(self.content)
        verses1 = content1.split('\n')

        content2 = remove_punctuations(other.content)
        verses2 = content2.split('\n')

        return verses1[0] == verses2[0]  # and verses1[1] == verses2[1]

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

