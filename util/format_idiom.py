# -*- coding: utf-8 -*-

import sys

from idiom import *


# =======================================================================
# main()
# =======================================================================


def main():
    reload(sys)
    sys.setdefaultencoding("utf-8")

    idioms = load_formatted_idiom_json('data/idioms.json')
    write_idioms_text_file('data/result/idioms.txt', idioms)


if __name__ == "__main__":
    main()
