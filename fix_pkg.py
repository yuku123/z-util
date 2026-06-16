#!/usr/bin/env python3
import os
import sys

if len(sys.argv) < 3:
    print("Usage: fix_pkg.py OLD_PKG NEW_PKG <files...>")
    sys.exit(1)

old_pkg = sys.argv[1]
new_pkg = sys.argv[2]
files = sys.argv[3:]

fixed = 0
for f in files:
    if not os.path.isfile(f):
        continue
    with open(f, 'r', encoding='utf-8', errors='replace') as fh:
        content = fh.read()
    new_content = content.replace(
        f'package {old_pkg};',
        f'package {new_pkg};'
    )
    if new_content != content:
        with open(f, 'w', encoding='utf-8') as fh:
            fh.write(new_content)
        fixed += 1
        print(f'Fixed: {os.path.basename(f)}')
print(f'Total: {fixed}')