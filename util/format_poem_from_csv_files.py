# -*- coding: utf-8 -*-

import sys
import csv

from poem import *


def parse_title(line):
    line.strip()
    title = line
    subtitle = ''
    idx = line.find(' ')
    if idx != -1:
        title = line[:idx]
        subtitle = line[idx+1:]

    return title.encode('utf-8'), subtitle.encode('utf-8')


def parse_content(line):
    line.strip()
    line = line.replace(' ', '')

    for c in PUNCTUATIONS:
        line = line.replace(c, c + '\n')

    l = len(line)
    if line[l-1] == '\n':
        line = line[:l-1]

    return line.encode('utf-8')


def parse_csv_file(csvfile):
    sys.stdout.write('processing ' + csvfile + '...')
    poems = []
    with open(csvfile.encode('utf-8')) as csv_fh:
        csv_reader = csv.reader(csv_fh, delimiter=',')
        lc = 0
        for row in csv_reader:
            lc += 1
            if lc == 1:
                continue

            try:
                title, subtitle = parse_title(row[0])
                period = row[1].encode('utf-8')
                author = row[2].encode('utf-8')
                content = parse_content(row[3])
            except Exception as e:
                print('error at line ' + str(lc) + ': ' + e.message)

            poem = Poem(title, subtitle, '', author, '', content, period, '', '')
            poems.append(poem)

    print(str(len(poems)) + ' poems processed')
    return poems


# =======================================================================
# main()
# =======================================================================


def main():
    reload(sys)
    sys.setdefaultencoding("utf-8")

    poems = parse_csv_file('data/csv/唐.csv')


if __name__ == "__main__":
    main()
