# -*- coding: utf-8 -*-

import sys
import json


# =======================================================================
# Idiom class
# =======================================================================


class Idiom:

    def __init__(self, content, pinyin, explanation, derivation, example):
        self.content = content
        self.pinyin = pinyin
        self.explanation = explanation
        self.derivation = derivation
        self.example = example

    def equals(self, other):
        return self.content == other.content

    def to_string(self):
        return self.content

    def display(self):
        print(self.content)
        print(self.pinyin)
        print(self.explanation)
        print(self.derivation)
        print(self.example)

    def get_json(self):
        return json.dumps(self, default=lambda o: o.__dict__, ensure_ascii=False, sort_keys=True, indent=4)


def idiom_dict(idiom):
    return idiom.__dict__


# =======================================================================
# Idiom util methods
# =======================================================================


def load_formatted_idiom_json(jsonfile):
    idioms = []
    with open(jsonfile) as fh:
        data = json.load(fh)
        for p in data:
            idiom = Idiom(p['word'], p['pinyin'], p['explanation'], p['derivation'], p['example'])
            idioms.append(idiom)

    print(str(len(idioms)) + " idioms were loaded.")

    # return poem list
    return idioms


def write_idioms_text_file(txtfile, idioms):
    with open(txtfile, 'w') as fh:
        for idiom in idioms:
            parts = idiom.content.split('，')
            for s in parts:
                fh.write(s + '\n')
