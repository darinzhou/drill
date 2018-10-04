# -*- coding: utf-8 -*-

import sys
import os
import sqlite3
import json

from idiom import *


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
    sql = 'CREATE TABLE idiom_table (' \
          'id INTEGER PRIMARY KEY AUTOINCREMENT,' \
          'content BLOB,' \
          'pinyin BLOB,' \
          'explanation BLOB,' \
          'derivation BLOB,' \
          'example BLOB)'
    conn.execute(sql)

    return conn


def save_idioms_to_db(idioms, conn):
    conn.text_factory = lambda x: unicode(x, 'utf-8', 'ignore')
    sql = 'INSERT INTO idiom_table (content, pinyin, explanation, derivation, example) VALUES (?,?,?,?,?)'

    # start
    conn.execute('BEGIN')

    # insert
    for p in idioms:
        conn.execute(sql, (p.content, p.pinyin, p.explanation, p.derivation, p.example))
    conn.commit()

    # end
    conn.execute('VACUUM')


def count_idioms(conn):
    sql = 'SELECT COUNT(*) FROM idiom_table'
    cursor = conn.execute(sql)
    for row in cursor:
        return row[0]
    return -1


def read_idiom(conn, content):
    conn.text_factory = lambda x: unicode(x, 'utf-8', 'ignore')

    idioms = []
    sql = "SELECT content, pinyin, explanation, derivation, example FROM idiom_table " \
          "WHERE content like '%" + content + "%'"
    cursor = conn.execute(sql)
    for row in cursor:
        idioms.append(Idiom(row[0], row[1], row[2], row[3], row[4]))

    return idioms


# =======================================================================
# build idiom.db
# =======================================================================

def build():
    conn = create_db('data/result/idioms.db')
    idioms = load_formatted_idiom_json('data/result/idioms.json')
    save_idioms_to_db(idioms, conn)
    conn.close()


# =======================================================================
# test
# =======================================================================
def test(text):
    conn = sqlite3.connect('data/result/idioms.db')

    pn = count_idioms(conn)
    print(str(pn) + " idioms in database")

    print("Idioms related to " + text + ":")
    idioms = read_idiom(conn, text)
    for p in idioms:
        p.display()

    conn.close()


# =======================================================================
# main()
# =======================================================================


def main():
    reload(sys)
    sys.setdefaultencoding("utf-8")

    build()
    test('百密一疏')


if __name__ == "__main__":
    main()
