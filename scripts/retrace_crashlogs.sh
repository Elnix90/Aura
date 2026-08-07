#!/usr/bin/env bash
set -euo pipefail

usage() {
  echo "Usage: $0 /path/to/mapping.txt /path/to/stacktrace.txt" >&2
  exit 1
}

die() {
  echo "Error: $*" >&2
  exit 1
}

mapping_file="${1:-}"
stacktrace_file="${2:-}"

[[ -n "$mapping_file" && -n "$stacktrace_file" ]] || usage

[[ -f "$mapping_file" ]] || die "'$mapping_file' is not a file"
[[ -f "$stacktrace_file" ]] || die "'$stacktrace_file' is not a file"

RETRACE_BIN="${RETRACE_BIN:-$HOME/Android/Sdk/cmdline-tools/latest/bin/retrace}"

if [[ ! -x "$RETRACE_BIN" ]]; then
  command -v retrace >/dev/null 2>&1 || die "retrace is not installed or not executable"
  RETRACE_BIN="$(command -v retrace)"
fi

"$RETRACE_BIN" "$mapping_file" "$stacktrace_file"


# usage:
# ./retrace_stacktrace.sh app/build/outputs/mapping/betaRelease/mapping.txt /path/to/crash.txt