package org.freebase;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

/**
 * replace GZIPInputStream to read concatenated gz file

 FileInputStream fis = new FileInputStream(zipFileName);
 BufferedReader reader = new BufferedReader(new InputStreamReader(
 new MultiMemberGZIPInputStream(fis)));
 */


public class MultiMemberGZIPInputStream extends GZIPInputStream {



    private MultiMemberGZIPInputStream parent;
    private MultiMemberGZIPInputStream child;
    private int size;
    private boolean eos;

    public MultiMemberGZIPInputStream(InputStream in, int size)
            throws IOException {
        // Wrap the stream in a PushbackInputStream…
        super(new PushbackInputStream(in, size), size);
        this.size = size;
    }

    public MultiMemberGZIPInputStream(InputStream in)
            throws IOException {
        // Wrap the stream in a PushbackInputStream…
        super(new PushbackInputStream(in, 1024));
        this.size = -1;
    }

    private MultiMemberGZIPInputStream(MultiMemberGZIPInputStream parent)
            throws IOException {
        super(parent.in);
        this.size = -1;
        this.parent = parent.parent == null ? parent : parent.parent;
        this.parent.child = this;
    }

    private MultiMemberGZIPInputStream(MultiMemberGZIPInputStream parent, int size)
            throws IOException {
        super(parent.in, size);
        this.size = size;
        this.parent = parent.parent == null ? parent : parent.parent;
        this.parent.child = this;
    }

    public int read(byte[] inputBuffer, int inputBufferOffset, int inputBufferLen)
            throws IOException {

        if (eos) {
            return -1;
        }
        if (this.child != null) {
            return this.child.read(inputBuffer, inputBufferOffset, inputBufferLen);
        }

        int charsRead = super.read(inputBuffer, inputBufferOffset, inputBufferLen);
        if (charsRead == -1) {
            // Push any remaining buffered data back onto the stream
            // If the stream is then not empty, use it to construct
            // a new instance of this class and delegate this and any
            // future calls to it…
            int n = inf.getRemaining() - 8;
            if (n > 0) {
                // More than 8 bytes remaining in deflater
                // First 8 are gzip trailer. Add the rest to
                // any un-read data…
                ((PushbackInputStream)this.in).unread(buf, len - n, n);
            } else {
                // Nothing in the buffer. We need to know whether or not
                // there is unread data available in the underlying stream
                // since the base class will not handle an empty file.
                // Read a byte to see if there is data and if so,
                // push it back onto the stream…
                byte[] b = new byte[1];
                int ret = in.read(b, 0, 1);
                if (ret == -1) {
                    eos = true;
                    return -1;
                } else {
                    ((PushbackInputStream)this.in).unread(b, 0, 1);
                }
            }

            MultiMemberGZIPInputStream child;
            if (this.size == -1) {
                child = new MultiMemberGZIPInputStream(this);
            } else {
                child = new MultiMemberGZIPInputStream(this, this.size);
            }
            return child.read(inputBuffer, inputBufferOffset, inputBufferLen);
        } else {
            return charsRead;
        }
    }
}
