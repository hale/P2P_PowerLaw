#!/bin/sh
cd doc

auml-new.pl protocol_connect
auml-new.pl protocol_send_file_list
auml-new.pl protocol_file_search
auml-new.pl protocol_acquire_file

wish protocol_connect.tk
wish protocol_send_file_list.tk
wish protocol_file_search.tk
wish protocol_acquire_file.tk

pdflatex -shell-escape report.tex
open report.pdf
