Sentence = 
That was the beginning of their long friendship, which continues today.

Constituent tree = 
(TOP (S (NP-SBJ (DT That))
            (VP (VBD was)
                (NP-PRD (NP (DT the)
                            (NN beginning))
                        (PP (IN of)
                            (NP (NP (PRP$ their)
                                    (JJ long)
                                    (NN friendship))
                                (, ,)
                                (SBAR (WHNP-1 (WDT which))
                                      (S (NP-SBJ (-NONE- *T*-1))
                                         (VP (VBZ continues)
                                             (NP-TMP (NN today)))))))))
            (. .)))


Dependency tree = 
1	That	that	DET	DT	_	2	nsubj	_	_
2	was	be	VERB	VBD	_	0	root	_	_
3	the	the	DET	DT	_	4	det	_	_
4	beginning	beginning	NOUN	NN	_	2	xcomp	_	_
5	of	of	ADP	IN	_	4	prep	_	_
6	their	they	PRON	PRP$	_	8	poss	_	_
7	long	long	ADJ	JJ	_	8	amod	_	_
8	friendship	friendship	NOUN	NN	_	5	pobj	_	_
9	,	,	PUNCT	,	_	8	punct	_	_
10	which	which	DET	WDT	_	12	dobj	_	_
11	*T*-1	*t*-1	NOUN	NN	_	12	nsubj	_	_
12	continues	continue	VERB	VBZ	_	8	rcmod	_	_
13	today	today	NOUN	NN	_	12	tmod	_	_
14	.	.	PUNCT	.	_	2	punct	_	_



1	That	That	DET	DT	_	2	nsubj	_	_	_
2	was	was	VERB	VBD	_	0	root	_	_	_
3	the	the	DET	DT	_	4	det	_	_	_
4	beginning	beginning	NOUN	NN	_	2	xcomp	_	_	_
5	of	of	ADP	IN	_	4	prep	_	_	_
6	their	their	PRON	PRP$	_	8	poss	_	_	_
7	long	long	ADJ	JJ	_	8	amod	_	_	_
8	friendship	friendship	NOUN	NN	_	5	pobj	_	_	_
9	,	,	PUNCT	,	_	8	punct	_	_	_
10	which	which	DET	WDT	_	11	nsubj	_	_	_
11	continues	continues	VERB	VBZ	_	8	rcmod	_	_	_
12	today	today	NOUN	NN	_	11	tmod	_	_	_
13	.	.	PUNCT	.	_	2	punct	_	_	_



SRL annotation = 
bc/phoenix/00/phoenix_0000@0000@phoenix@bc@en@on 451 1 gold be-v be.01 ----- 1:0-rel 0:1-ARG1 2:2-ARG2
bc/phoenix/00/phoenix_0000@0000@phoenix@bc@en@on 451 11 gold continue-v continue.01 ----- 11:0-rel 10:0-ARG1 12:1-ARGM-TMP 10:0*5:1-LINK-SLC
