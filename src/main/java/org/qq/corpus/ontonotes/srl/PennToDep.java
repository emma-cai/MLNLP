package org.qq.corpus.ontonotes.srl;

import edu.emory.clir.clearnlp.bin.C2DConvert;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;

/**
 * Created by qingqingcai on 7/21/15.
 */
public class PennToDep {

    public static final String ontonotesPath = "data/ontonotes/treeBank/english";
    public static final String headRulePath = "src/main/resources/headrule_en_stanford.txt";
    public static final String[] suffixes = {"parse"};
    public static void main(String... args) {

        File ontonotesDir = new File(ontonotesPath);
        File headRuleFile = new File(headRulePath);

        FileUtils.listFilesAndDirs(ontonotesDir,
                new NotFileFilter(TrueFileFilter.INSTANCE), DirectoryFileFilter.DIRECTORY).stream().filter(
                dir -> FileUtils.listFiles(dir, suffixes, false).size() > 0).forEach(dir -> C2DConvert.main(new String[]{
                "-h", headRuleFile.getAbsolutePath(),
                "-i", dir.getAbsolutePath()
        }));
    }
}
