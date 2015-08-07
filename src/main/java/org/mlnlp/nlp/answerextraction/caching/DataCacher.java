package org.mlnlp.nlp.answerextraction.caching;

import org.maochen.nlp.datastructure.DTree;

/**
 * Allowing the caching of data, e.g. parser output.
 */
public interface DataCacher {

    /**
     * Return a parse for the given sentence. If parse is not cached, uses nlpService to parse the input.
     * Depending on the implementation, if you know that all parses are cached, you can set nlpService to null.
     * @param sentence
     * @return
     */
    DTree parseSentence(String sentence/**, ElizaNLPService nlpService**/);

    void close();
}
