package org.freebase;

import java.io.*;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

/**
 * replace GZIPInputStream to read concatenated gz file

 FileInputStream fis = new FileInputStream(zipFileName);
 BufferedReader reader = new BufferedReader(new InputStreamReader(
 new MultiMemberGZIPInputStream(fis)));
 */


public class ReadGz extends GZIPInputStream {
    private ReadGz parent;
    private ReadGz child;
    private int size;
    private boolean eos;

    public ReadGz(InputStream in, int size)
            throws IOException {
        // Wrap the stream in a PushbackInputStream…
        super(new PushbackInputStream(in, size), size);
        this.size = size;
    }

    public ReadGz(InputStream in)
            throws IOException {
        // Wrap the stream in a PushbackInputStream…
        super(new PushbackInputStream(in, 1024));
        this.size = -1;
    }

    private ReadGz(ReadGz parent)
            throws IOException {
        super(parent.in);
        this.size = -1;
        this.parent = parent.parent == null ? parent : parent.parent;
        this.parent.child = this;
    }

    private ReadGz(ReadGz parent, int size)
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

            ReadGz child;
            if (this.size == -1) {
                child = new ReadGz(this);
            } else {
                child = new ReadGz(this, this.size);
            }
            return child.read(inputBuffer, inputBufferOffset, inputBufferLen);
        } else {
            return charsRead;
        }
    }

    public static void main(String[] args) throws IOException {

        FileInputStream fileInputStream = new FileInputStream("/Users/qingqingcai/Downloads/freebase-rdf" +
                "-latest.gz");
        GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
        InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        int lines = 1;
        try {
            String content;
            while ((content = bufferedReader.readLine()) != null) {
                String[] contentArray = content.split("\t");
//            if (contentArray.length == 4 && contentArray[1].contains("type") && contentArray[2].contains("code")) {
//                System.out.println(content);
//            }

//            if (contentArray.length == 4
//                    && contentArray[2].contains("@en")
//                    ) {
//                System.out.println(content);
//            }


            if (contentArray.length == 4
//                    && (contentArray[1].contains("location.postal_code>")
                        && contentArray[1].contains("location.mailing_address.street_address"))
                     {
                System.out.println(content);
            }

//            if (contentArray.length == 4
//                    && (contentArray[1].equals("<http://rdf.freebase.com/ns/type.object.type>"))
////                    && (contentArray[2].contains("street_address"))
//                    ) {
//                System.out.println(content);
//            }


//                if (contentArray.length == 4
//                        && (/**contentArray[1].contains("type.object.name") && **/contentArray[2].contains("\"") && contentArray[2].contains("@en"))) {
//                    System.out.println(content);
//                }
//                lines++;
            }
        } finally {
            fileInputStream.close();
            gzipInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        }
    }
}