package org.mlnlp.nlp.answerextraction.caching;

import org.maochen.nlp.datastructure.DTree;
import org.maochen.nlp.datastructure.LangTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Caches using an H2 Database.
 */
public class H2DataCacher implements DataCacher {

    private static final Logger LOG = LoggerFactory.getLogger(H2DataCacher.class);

    private final CachingService cachingService;

    public H2DataCacher(String dirPath, String fileName) {
        cachingService = new CachingService();

        File dirPathFile = new File(dirPath);

        if (!dirPathFile.exists()) {
            dirPathFile.mkdirs();
        }

        File fullPathFile = new File(dirPathFile, fileName);

        if (dirPathFile.exists()) {
            cachingService.init(fullPathFile.getAbsolutePath());
        } else {
            throw new IllegalStateException("Cannot create directory: " + dirPathFile.getAbsolutePath());
        }
    }

    /**
     * Return a parse for the given sentence. If parse is not cached, uses nlpService to parse the input.
     * If you know that all parses are cached, you can set nlpService to null.
     *
     * @param sentence
     * @return
     */
    @Override
    public DTree parseSentence(String sentence/**, ElizaNLPService nlpService**/) {
        DTree dtree;

        // Try cache.
        String conllx = cachingService.getParseFromCache(sentence);

        // Cache failed; use nlpService.
        if (conllx == null) {

//            DTree tree = nlpService.parse(sentence, false);
//
//            if ("?".equals(tree.get(tree.size()-1).getForm())) {
//                String qt = nlpService.getQuestionType(tree);
//                if (!tree.getRoots().isEmpty()) {
//                    tree.getRoots().get(0).addFeature("question_type", qt);
//                }
//            }
//
//            conllx = tree.toString();
//            LOG.info("Using NLP Service: " + sentence);
//
//            // Write to the cache.
//            Map<String, String> data = new HashMap<>();
//            data.put(sentence, conllx);
//            cachingService.put(data);
            LOG.error("Caching failed!");
        } else {
            LOG.info("Using cached parse.");
        }

        try {
            dtree = LangTools.getDTreeFromCoNLLXString(conllx);
        } catch (Exception e) {
            String msg = "Error: Unable to get parse for: \n" + sentence + "\n" + conllx;
            LOG.error(msg);
            throw new IllegalStateException(msg, e);
        }

        if (dtree == null) {
            String msg = "Error: Parse is null for: \n" + sentence + "\n" + conllx;
            LOG.error(msg);
            throw new IllegalStateException(msg);
        }

        dtree.setOriginalSentence(sentence);
        return dtree;
    }

    @Override
    public void close() {
        cachingService.closeConnection();
    }
}
