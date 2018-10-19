# -*- coding: utf-8 -*-

import sys
import os
import json

from poem import *
from format_poem_from_csv_files import parse_csv_file


# =======================================================================
# compare
# =======================================================================

def compare(jsonfile, jsonfile1):
    print("Checking poems not in " + jsonfile + " but in " + jsonfile1 + "...")

    poems = load_formatted_poem_json(jsonfile)
    poems1 = load_formatted_poem_json(jsonfile1)

    poems_in = []
    poems_not_in = []

    count = 0
    for p1 in poems1:
        found = False

        for p in poems:
            if p1.equals(p):
                found = True
                poems_in.append(p)
                break

        if not found:
            count += 1
            p1.display()
            poems_not_in.append(p1)

    print("Found " + str(count) + " poems not in " + jsonfile + " but in " + jsonfile1)
    return poems_in, poems_not_in

# =======================================================================
# main()
# =======================================================================


def main():
    reload(sys)
    sys.setdefaultencoding("utf-8")

    # pi, pni = compare('data/result/intermediate.json', 'data/result/qianjiashi.json')
    # write_poems_json_file('data/result/qianjiashi-not-in.json', pni)

    pi, pni = compare('data/result/intermediate.json', 'data/result/songci300.json')
    write_poems_json_file('data/result/songci300-not-in.json', pni)


if __name__ == "__main__":
    main()
