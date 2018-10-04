# -*- coding: utf-8 -*-

import sys
import os
import sqlite3
import json

from poem import *
from format_poem_from_csv_files import parse_csv_file


# =======================================================================
# database utils
# =======================================================================


def create_db(dbfile):
    # remove old db
    if os.path.isfile(dbfile):
        try:
            os.remove(dbfile)
        except Exception as e:
            print(e)

    # new db
    conn = sqlite3.connect(dbfile, isolation_level=None)

    # create table
    sql = 'CREATE TABLE poem_sentence_table (' \
          'id INTEGER PRIMARY KEY AUTOINCREMENT,' \
          'title BLOB,' \
          'subtitle BLOB,' \
          'author BLOB,' \
          'period BLOB,' \
          'sn INTEGER,' \
          'wordcount INTEGER,' \
          'sentence BLOB)'
    conn.execute(sql)

    return conn


def poem_content_to_sentence(content):
    return content.split('\n')


def end_with_punctuation(s):
    return s[len(s)-1] in PUNCTUATIONS


def word_count_in_sentence(s):
    word_count = len(s)
    if end_with_punctuation(s):
        word_count -= 1

    return word_count


def save_poems_to_db(poems, conn):
    conn.text_factory = lambda x: unicode(x, 'utf-8', 'ignore')
    sql = 'INSERT INTO poem_sentence_table (title, subtitle, author, period, sn, wordcount, sentence) ' \
          'VALUES (?,?,?,?,?,?,?)'

    # start
    conn.execute('BEGIN')

    # insert
    for p in poems:
        sn = 1
        title = p.title
        subtitle = p.subtitle
        author = p.author
        period = p.period
        sentences = poem_content_to_sentence(p.content)
        for s in sentences:
            word_count = word_count_in_sentence(s)
            conn.execute(sql, (title, subtitle, author, period, sn, word_count, s))
            sn += 1
    conn.commit()

    # end
    conn.execute('VACUUM')


def count_poems(conn):
    sql = 'SELECT COUNT(*) FROM poem_sentence_table WHERE sn=1'
    cursor = conn.execute(sql)
    for row in cursor:
        return row[0]
    return -1


def build_poems(cursor, title_in):
    poems = []

    title = ''
    subtitle = ''
    author = ''
    period = ''
    content = ''
    skip = False
    for row in cursor:
        # start a new poem
        if row[5] == 1:
            # end previous poem if exists
            if content:
                poems.append(Poem(title, subtitle, '', author, '', content, period, '', ''))
                content = ''

            title = row[0]
            if title_in and title_in not in title and title not in title_in:
                skip = True
                continue

            skip = False
            subtitle = row[1]
            author = row[2]
            period = row[3]

        if skip:
            continue

        if content:
            content += '\n'
        content += row[4]

    # last poem
    if content:
        poems.append(Poem(title, subtitle, '', author, '', content, period, '', ''))

    return poems


def read_poem(conn, title, subtitle, author):
    conn.text_factory = lambda x: unicode(x, 'utf-8', 'ignore')

    sql = 'SELECT title, subtitle, author, period, sentence, sn FROM poem_sentence_table'

    if title:
        title1 = "title='" + title + "'"
        title2 = "title like '%" + title + "%'"

    if subtitle:
        subtitle = "subtitle='" + subtitle + "'"

    if author:
        author = "author='" + author + "'"

    where = author
    if subtitle:
        if where:
            where = where + ' AND '
        where += subtitle

    if title:
        where0 = where
        if where0:
            where0 = where + ' AND '

        where1 = where0 + title1
        sql1 = sql + ' WHERE ' + where1
        cursor = conn.execute(sql1)
        poems = build_poems(cursor, title)
        if len(poems) > 0:
            return poems

        where2 = where0 + title2
        sql2 = sql + ' WHERE ' + where2
        cursor = conn.execute(sql2)
        poems = build_poems(cursor, title)
        if len(poems) > 0:
            return poems

    if where:
        sql += ' WHERE ' + where
    cursor = conn.execute(sql)
    poems = build_poems(cursor, title)

    return poems


def read_poem_with_sentence_and_author(conn, sentence, sn, author):
    conn.text_factory = lambda x: unicode(x, 'utf-8', 'ignore')

    sql = 'SELECT title, subtitle, author, period, sentence, sn FROM poem_sentence_table WHERE ' \
          'sn=? AND sentence=? AND author=?'

    cursor = conn.execute(sql, (sn, sentence, author))
    poems = build_poems(cursor, '')

    return poems


# =======================================================================
# build basic.db
# =======================================================================

def build_basic():
    poems = load_formatted_poem_json('data/result/basic.json')
    conn = create_db('data/result/basic.db')
    save_poems_to_db(poems, conn)
    conn.close()


# =======================================================================
# build intermediate.db
# =======================================================================

def build_intermediate():
    poems = load_formatted_poem_json('data/result/intermediate.json')
    conn = create_db('data/result/intermediate.db')
    save_poems_to_db(poems, conn)
    conn.close()


# =======================================================================
# build advanced.db
# =======================================================================

def add_period(poems):
    conn = sqlite3.connect('data/result/poem_lib.db')
    for p in poems:
        sys.stdout.write('.')
        if not p.period:

            ps = read_poem(conn, p.title, '', p.author)
            if len(ps) > 0:
                p.period = ps[0].period
            else:
                sentence = p.content.split('\n')[0]
                ps = read_poem_with_sentence_and_author(conn, sentence, 1, p.author)
                if len(ps) > 0:
                    p.period = ps[0].period
                else:
                    sentence = p.content.split('\n')[1]
                    ps = read_poem_with_sentence_and_author(conn, sentence, 2, p.author)
                    if len(ps) > 0:
                        p.period = ps[0].period
                    else:
                        ps = read_poem(conn, '', '', p.author)
                        if len(ps) > 0:
                            p.period = ps[0].period
                        else:
                            print("not found: " + p.title + " by " + p.author)
    conn.close()


def build_advanced():
    poems = load_formatted_poem_json('data/result/advanced.json')
    add_period(poems)
    write_poems_json_file('data/result/advanced.json', poems)
    conn = create_db('data/result/advanced.db')
    save_poems_to_db(poems, conn)
    conn.close()


# =======================================================================
# build poem_lib.db
# =======================================================================

def build_poem_lib():
    conn = create_db('data/result/poem_lib.db')
    count = 0
    for fn in os.listdir('data/csv'):
        if fn.endswith(".csv"):
            filename = 'data/csv/' + fn
            poems = parse_csv_file(filename)
            count += len(poems)
            save_poems_to_db(poems, conn)
    conn.close()
    print('Total poem count: ' + str(count))


# =======================================================================
# test
# =======================================================================
def test():
    conn = sqlite3.connect('data/result/advanced.db')

    pn = count_poems(conn)
    print(str(pn) + " poems in database")

    poems = read_poem(conn, '', '', '')
    for p in poems:
        p.display()

    conn.close()


# =======================================================================
# main()
# =======================================================================


def main():
    reload(sys)
    sys.setdefaultencoding("utf-8")

    # build_poem_lib()
    # build_basic()
    # build_intermediate()
    build_advanced()

    test()


if __name__ == "__main__":
    main()
