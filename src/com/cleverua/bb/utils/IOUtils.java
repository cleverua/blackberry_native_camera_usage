package com.cleverua.bb.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.io.file.FileIOException;
import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.EncodedImage;

/**
 * A bunch of convenient methods for file IO manipulations.
 * 
 * @author Vit Khudenko, vit@cleverua.com
 */
public class IOUtils {

	private static final String TMP_EXT = ".tmp";

	/**
	 * Safely closes {@link InputStream} stream.
	 * 
	 * <p>
	 * If stream is not null, the method calls <code>close()</code> for the
	 * stream.<br />
	 * If any {@link Exception} occurs during <code>close()</code> call, that
	 * exception is silently caught.
	 * </p>
	 * 
	 * @param stream
	 *            {@link InputStream} instance to be closed.
	 */
	public static void safelyCloseStream(InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
				stream = null;
			} catch (Exception e) { /* that's ok */
			}
		}
	}

	/**
	 * Safely closes {@link OutputStream} stream.
	 * 
	 * <p>
	 * If stream is not null, the method calls <code>close()</code> for the
	 * stream.<br />
	 * If any {@link Exception} occurs during <code>close()</code> call, that
	 * exception is caught and the only action being made is putting a log
	 * message about the got exception.
	 * </p>
	 * 
	 * <p>
	 * <b>IMPORTANT:</b><br />
	 * Despite <code>close()</code> call also invokes <code>flush()</code>
	 * first, clients should not relay on this behavior and must call
	 * <code>flush()</code> explicitly before calling
	 * <code>safelyCloseStream(OutputStream stream)</code> instead. Otherwise
	 * any useful exception <code>flush()</code> may throw will not be passed to
	 * the client and the stream will remain open.
	 * </p>
	 * 
	 * @param stream
	 *            {@link OutputStream} instance to be closed.
	 */
	public static void safelyCloseStream(OutputStream stream) {
		if (stream != null) {
			try {
				stream.close();
				stream = null;
			} catch (Exception e) { /* n/a */
			}
		}
	}

	/**
	 * Safely closes {@link FileConnection} stream.
	 * 
	 * <p>
	 * If stream is not null, the method calls <code>close()</code> for the
	 * stream.<br />
	 * If any {@link Exception} occurs during <code>close()</code> call, that
	 * exception is silently caught.
	 * </p>
	 * 
	 * @param stream
	 *            {@link FileConnection} instance to be closed.
	 */
	public static void safelyCloseStream(FileConnection stream) {
		if (stream != null) {
			try {
				stream.close();
				stream = null;
			} catch (Exception e) { /* that's ok */
			}
		}
	}

	/**
	 * Safely closes {@link OutputStreamWriter} stream.
	 * 
	 * <p>
	 * If stream is not null, the method calls <code>close()</code> for the
	 * stream.<br />
	 * If any {@link Exception} occurs during <code>close()</code> call, that
	 * exception is caught and the only action being made is putting a log
	 * message about the got exception.
	 * </p>
	 * 
	 * <p>
	 * <b>IMPORTANT:</b><br />
	 * Despite <code>close()</code> call also invokes <code>flush()</code>
	 * first, clients should not relay on this behavior and must call
	 * <code>flush()</code> explicitly before calling
	 * <code>safelyCloseStream(OutputStreamWriter stream)</code> instead.
	 * Otherwise any useful exception <code>flush()</code> may throw will not be
	 * passed to the client and the stream will remain open.
	 * </p>
	 * 
	 * @param stream
	 *            {@link OutputStreamWriter} instance to be closed.
	 */
	public static void safelyCloseStream(OutputStreamWriter stream) {
		if (stream != null) {
			try {
				stream.close();
				stream = null;
			} catch (Exception e) { /* n/a */
			}
		}
	}

	/**
	 * Deletes the file or directory specified in the <code>url</code>
	 * parameter. If the target does not exist, then the method does nothing.
	 * 
	 * @param url
	 *            - URL to a file or a directory to be deleted, e.g.
	 *            <code>"file:///SDCard/file.txt"</code>.
	 * 
	 * @throws IOException
	 *             <ul>
	 *             <li>if the <code>url</code> is invalid.</li>
	 *             <li>if the target is a directory and it is not empty, the
	 *             target is unaccessible, or an unspecified error occurs
	 *             preventing deletion of the target.</li>
	 *             </ul>
	 */
	public static void delete(String url) throws IOException {
		FileConnection fc = null;
		try {
			fc = (FileConnection) Connector.open(url);
			if (fc.exists()) {
				fc.delete();
			}
		} finally {
			safelyCloseStream(fc);
		}
	}

	/**
	 * Renames the selected file or directory to a new name in the same
	 * directory. If the target does not exist, then the method does nothing.
	 * 
	 * @param url
	 *            - URL to the file or directory to be renamed, e.g.
	 *            <code>"file:///SDCard/file.txt"</code>.
	 * 
	 * @param newName
	 *            - The new name of the file or directory. The name must not
	 *            contain any path specification; the file or directory remains
	 *            in its same directory as before this method call.
	 * 
	 * @throws IOException
	 *             <ul>
	 *             <li>if the <code>url</code> is invalid.</li>
	 *             <li>if the connection's target for the <code>url</code> is
	 *             not accessible, a file or directory already exists by the
	 *             <code>newName</code>, or <code>newName</code> is an invalid
	 *             filename for the platform (e.g. contains characters invalid
	 *             in a filename on the platform)</li>
	 *             </ul>
	 * @throws NullPointerException
	 *             if <code>newName</code> is null.
	 * @throws IllegalArgumentException
	 *             if <code>newName</code> contains any path specification.
	 */
	public static void rename(String url, String newName) throws IOException {
		FileConnection fc = null;
		try {
			fc = (FileConnection) Connector.open(url);
			if (fc.exists()) {
				fc.rename(newName);
			}
		} finally {
			safelyCloseStream(fc);
		}
	}

	/**
	 * Copies a file. If the destination file has been already present, then it
	 * is overwritten.
	 * 
	 * @param sourceFileUrl
	 *            - url of the source file.
	 * @param destinationFileUrl
	 *            - url of the destination file.
	 * @throws IOException
	 */
	public static void copyFile(String sourceFileUrl, String destinationFileUrl)
			throws IOException {
		InputStream is = null;
		OutputStream os = null;
		FileConnection source = null;
		FileConnection destination = null;
		FileConnection destinationTmp = null;

		try {

			source = (FileConnection) Connector.open(sourceFileUrl,
					Connector.READ);
			destination = (FileConnection) Connector.open(destinationFileUrl);

			if (destination.exists()) {
				// truncate does not work if file is encrypted via SDCard
				// encryption (has ".rem" suffix)!
				// destination.truncate(0);

				destinationTmp = (FileConnection) Connector
						.open(destinationFileUrl + TMP_EXT);

				if (destinationTmp.exists()) {
					destinationTmp.delete(); /* just in case */
				}
				destinationTmp.create();

				try {
					is = source.openInputStream();
					os = destinationTmp.openOutputStream();
					copyData(is, os);
				} catch (IOException e) {
					safelyCloseStream(os);
					try {
						destinationTmp.delete();
					} catch (IOException e1) {
						/* do nothing here */
					}
					throw e;
				}

				String destinationFileName = destination.getName();
				destination.delete();
				destinationTmp.rename(destinationFileName);

			} else {
				destination.create();
				is = source.openInputStream();
				os = destination.openOutputStream();
				copyData(is, os);
			}

		} finally {
			safelyCloseStream(is);
			safelyCloseStream(os);
			safelyCloseStream(source);
			safelyCloseStream(destination);
			safelyCloseStream(destinationTmp);
		}
	}

	/**
	 * Saves byte array data to file with a given url. If the destination file
	 * has been already present, then it is overwritten.
	 * 
	 * @param data
	 *            - Array of bytes to save.
	 * @param url
	 *            - url of the destination file.
	 * @throws IOException
	 *             <ul>
	 *             <li>if the <code>url</code> is invalid.</li>
	 *             <li>if the target file system is not accessible or the data
	 *             array size is greater than free memory that is available on
	 *             the file system the file resides on.</li>
	 *             <li>if an I/O error occurs.</li>
	 *             <li>if url has a trailing "/" to denote a directory, or an
	 *             unspecified error occurs preventing creation of the file.</li>
	 *             </ul>
	 */
	public static void saveDataToFile(String url, byte[] data)
			throws IOException {
		FileConnection fc = null;
		FileConnection tmp = null;
		OutputStream out = null;

		try {
			fc = (FileConnection) Connector.open(url);

			// check for available space
			if (fc.availableSize() < data.length) {
				throw new FileIOException(FileIOException.FILESYSTEM_FULL);
			}

			if (fc.exists()) {

				tmp = (FileConnection) Connector.open(url + TMP_EXT);

				if (tmp.exists()) {
					tmp.delete(); /* just in case */
				}
				tmp.create();

				try {
					out = tmp.openOutputStream();
					out.write(data);
					out.flush();
				} catch (IOException e) {
					safelyCloseStream(out);
					try {
						tmp.delete();
					} catch (IOException e1) {
						/* do nothing here */
					}
					throw e;
				}

				String originalFileName = fc.getName();
				fc.delete();
				tmp.rename(originalFileName);

			} else {
				fc.create();
				out = fc.openOutputStream();
				out.write(data);
				out.flush();
			}

		} finally {
			safelyCloseStream(out);
			safelyCloseStream(fc);
			safelyCloseStream(tmp);
		}
	}

	/**
	 * Reads file data and returns it as a byte array. File should be present,
	 * otherwise IOException is thrown.
	 * 
	 * @param url
	 *            - url of the source file.
	 * @return Array of bytes.
	 * @throws IOException
	 *             <ul>
	 *             <li>if the <code>url</code> is invalid.</li>
	 *             <li>if an I/O error occurs, if the method is invoked on a
	 *             directory, the file does not yet exist, or the connection's
	 *             target is not accessible.</li>
	 *             </ul>
	 */
	public static byte[] getFileData(String url) throws IOException {
		FileConnection fc = null;
		InputStream in = null;

		try {
			fc = (FileConnection) Connector.open(url);
			in = fc.openInputStream();
			byte[] data = new byte[(int) fc.fileSize()];
			in.read(data);
			return data;
		} finally {
			safelyCloseStream(in);
			safelyCloseStream(fc);
		}
	}

	private static void copyData(InputStream source, OutputStream destination)
			throws IOException {
		byte[] buf = new byte[1024];
		int len;
		while ((len = source.read(buf)) > 0) {
			destination.write(buf, 0, len);
		}
		destination.flush();
	}

	public static void createDir(String dirPath) throws IOException {
		FileConnection fc = null;

		try {
			fc = (FileConnection) Connector.open(dirPath);
			if (!fc.exists()) {
				fc.mkdir();
			}

		} finally {
			safelyCloseStream(fc);
		}
	}

	/**
	 * Creates the directory (if it does not exist) from <b>dirPath</b> including it's ancestors.
	 * Note that only directories from <b>'file:///'</b> to the last <b>'/'</b> character 
	 * in the <b>dirPath</b> will be created. 
	 * @param dirPath
	 *     - path of directory to be created (completed by '/' character).
	 * @throws IOException
	 *     if an I/O error occurs or the connection's
     *     target is not accessible.
     */
	public static void createDirIncludingAncestors(String dirPath)
			throws IOException {
		int index = dirPath.indexOf(":///");
		if (index == -1) {
			throw new IllegalArgumentException();
		}
		String rootOfPath = dirPath.substring(0, index + 4); // e.g. "file:///"
		String restOfPath = dirPath.substring(rootOfPath.length());
		int solidusIndex = -1;
		while (true) {
			solidusIndex = restOfPath.indexOf(Characters.SOLIDUS,
					solidusIndex + 1);
			if (solidusIndex < 0) {
				break;
			}
			createDir(rootOfPath + restOfPath.substring(0, solidusIndex + 1));
		}
	}

	public static EncodedImage resizeImage(String file, int toWidth,
			int toHeight) throws Exception {
		InputStream in = null;
		FileConnection fc = null;
		try {
			fc = (FileConnection) Connector.open(file);
			in = fc.openInputStream();
			byte[] data = new byte[(int) fc.fileSize()];
			in.read(data);

			EncodedImage eImage = EncodedImage.createEncodedImage(data, 0,
					data.length);

			int scaleX = Fixed32.div(Fixed32.toFP(eImage.getWidth()), Fixed32
					.toFP(toWidth));
			int scaleY = Fixed32.div(Fixed32.toFP(eImage.getHeight()), Fixed32
					.toFP(toHeight));

			int scale = (scaleX > scaleY) ? scaleX : scaleY;

			return eImage.scaleImage32(scale, scale);

		} finally {
			safelyCloseStream(in);
			safelyCloseStream(fc);
		}
	}
}
