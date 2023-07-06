package apistagefse.base.stream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author b.amoruso
 */
public class StreamOp {

    private File f;

    public StreamOp(File f) {
        this.f = f;
    }

    public static File makeDirectory(File dir) {
        if (!dir.exists())
            dir.mkdirs();

        return dir;
    }

    /**
     * Make a directory by starting from given parent directory
     *
     * @param parent is the directory where it makes sub-directory by date
     * @param flat   if true it makes a single sub-directory with
     *               <code>yyyyMMdd</code> format, if false it makes a
     *               sub-directory and a child directory of this one with
     *               <code>yyyy</code>/<code>MM</code>/<code>dd</code> format
     * @return the handle to created sub-directory
     * @throws IOException if parent directory isn't a directory or it doesn't exist
     */
    public static File makeDirectoryByDate(File parent, boolean flat) throws IOException {
        if (!parent.isDirectory())
            throw new IOException("Parent isn't a directory or it doesn't exist");

        Date date = new Date();
        File dir = null;
        if (flat) {
            String name = new SimpleDateFormat("yyyyMMdd").format(date);

            dir = new File(parent, name);
        } else {
            String name = new SimpleDateFormat("yyyy").format(date);

            File y = new File(parent, name);
            File m = new File(y, new SimpleDateFormat("MM").format(date));
            dir = new File(m, new SimpleDateFormat("dd").format(date));
        }

        dir.mkdirs();

        return dir;
    }

    public File moveTo(File dest) throws IOException {
        Files.move(f.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return dest;
    }

    public StreamDigest toDigest() throws FileNotFoundException {
        return new StreamDigest(f);
    }

    public void write(byte[] i)
            throws FileNotFoundException, IOException {

        write(new ByteArrayInputStream(i));
    }

    public void write(InputStream i)
            throws FileNotFoundException, IOException {

        byte[] buffer = new byte[8 * 1024];

        // Try to read out the stream...
        try (InputStream in = new BufferedInputStream(i);
             OutputStream out = new FileOutputStream(f)) {

            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1)
                out.write(buffer, 0, bytesRead);

            out.flush();
            out.close();
            in.close();
        }

    }

    public File getFile() {
        return f;
    }

}
