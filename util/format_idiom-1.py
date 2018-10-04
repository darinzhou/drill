# -*- coding: utf-8 -*-

import sys

from idiom import *


# =======================================================================
# paring chengyu text file
# =======================================================================


def parse_idiom_text_file(txtfile):

    print("Parsing " + txtfile + "...")

    # create empty list for poems
    idioms = []

    # read file and parse line by line
    with open(txtfile, "r") as fh:

        # parse line by line
        for line in fh:

            # remove spaces
            line = line.strip()

            # empty line
            if not line:
                continue

            parts = line.split('	')
            content = parts[0].encode('utf-8')
            pinyin = parts[1].encode('utf-8')
            explanation = parts[2].encode('utf-8')

            idiom = Idiom(content, pinyin, explanation)
            idioms.append(idiom)

    print(str(len(idioms)) + " poems were parsed.")

    # return poem list
    return idioms


# =======================================================================
# main()
# =======================================================================


def main():
    reload(sys)
    sys.setdefaultencoding("utf-8")

    idioms = parse_idiom_text_file('data/13279成语.txt')
    write_idioms_json_file('data/result/idioms.json', idioms)
    write_idioms_text_file('data/result/idioms.txt', idioms)


if __name__ == "__main__":
    main()
