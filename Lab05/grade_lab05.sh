#!/bin/sh
# Compile in a temporary directory so stale .class files cannot affect the score.
set -u
cd "$(dirname "$0")" || exit 1

build_dir=$(mktemp -d "${TMPDIR:-/tmp}/lab05-grader.XXXXXXXX") || exit 1
trap 'rm -rf "$build_dir"' EXIT HUP INT TERM

if ! command -v javac >/dev/null 2>&1 || ! command -v java >/dev/null 2>&1; then
    printf '%s\n' 'Java JDK not found: javac and java are required.' 'FINAL SCORE: 0 / 100 (grading unavailable)'
    exit 1
fi

if ! javac -Xmaxerrs 8 -d "$build_dir" Lab05.java >"$build_dir/compile.log" 2>&1; then
    printf '%s\n' 'Compilation failed. Fix the errors below and run the grader again:'
    cat "$build_dir/compile.log"
    printf '%s\n' 'FINAL SCORE: 0 / 100'
    exit 0
fi

if ! javac -Xmaxerrs 8 -cp "$build_dir" -d "$build_dir" Lab05Tester.java >"$build_dir/compile.log" 2>&1; then
    printf '%s\n' 'The grader could not compile against Lab05.java. Check method signatures:'
    cat "$build_dir/compile.log"
    printf '%s\n' 'FINAL SCORE: 0 / 100'
    exit 0
fi

java -cp "$build_dir" Lab05Tester
