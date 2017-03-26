package com.yiju.ClassClockRoom.util.sdcard;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

class FileHelper {
	private static final int FILE_BUFFER_SIZE = 51200;

	private static boolean fileIsExist(String filePath) {
		if (filePath == null || filePath.length() < 1) {
			return false;
		}

		File f = new File(filePath);
		return f.exists();
	}

	private static InputStream readFile(String filePath) {
		if (null == filePath) {
			return null;
		}

		InputStream is;

		try {
			if (fileIsExist(filePath)) {
				File f = new File(filePath);
				is = new FileInputStream(f);
			} else {
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return is;
	}

	private static boolean createDirectory(String filePath) {
		if (null == filePath) {
			return false;
		}

		File file = new File(filePath);

		return file.exists() || file.mkdirs();

	}

	private static boolean deleteDirectory(String filePath) {
		if (null == filePath) {
			return false;
		}

		File file = new File(filePath);

		if (!file.exists()) {
			return false;
		}

		if (file.isDirectory()) {
			File[] list = file.listFiles();

			for (File aList : list) {
				if (aList.isDirectory()) {
					deleteDirectory(aList.getAbsolutePath());
				} else {
					aList.delete();
				}
			}
		}

		file.delete();
		return true;
	}

	public static boolean writeFile(String filePath, InputStream inputStream) {

		if (null == filePath || filePath.length() < 1) {
			return false;
		}

		try {
			File file = new File(filePath);
			if (file.exists()) {
				deleteDirectory(filePath);
			}

			String pth = filePath.substring(0, filePath.lastIndexOf("/"));
			boolean ret = createDirectory(pth);
			if (!ret) {
				return false;
			}

//			boolean ret1 = file.createNewFile();

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int c = inputStream.read(buf);
			while (-1 != c) {
				fileOutputStream.write(buf, 0, c);
				c = inputStream.read(buf);
			}

			fileOutputStream.flush();
			fileOutputStream.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}

	public static boolean writeFile(String filePath, String fileContent) {
		return writeFile(filePath, fileContent, false);
	}

	private static boolean writeFile(String filePath, String fileContent,
									 boolean append) {
		if (null == filePath || fileContent == null || filePath.length() < 1
				|| fileContent.length() < 1) {
			return false;
		}

		try {
			File file = new File(filePath);
			if (!file.exists()) {
				if (!file.createNewFile()) {
					return false;
				}
			}

			BufferedWriter output = new BufferedWriter(new FileWriter(file,
					append));
			output.write(fileContent);
			output.flush();
			output.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}

		return true;
	}

	public static long getFileSize(String filePath) {
		if (null == filePath) {
			return 0;
		}

		File file = new File(filePath);
		if (!file.exists()) {
			return 0;
		}

		return file.length();
	}

	public static long getFileModifyTime(String filePath) {
		if (null == filePath) {
			return 0;
		}

		File file = new File(filePath);
		if (!file.exists()) {
			return 0;
		}

		return file.lastModified();
	}

	public static boolean setFileModifyTime(String filePath, long modifyTime) {
		if (null == filePath) {
			return false;
		}

		File file = new File(filePath);
		return file.exists() && file.setLastModified(modifyTime);

	}

	public static boolean copyFile(ContentResolver cr, String fromPath,
			String destUri) {
		if (null == cr || null == fromPath || fromPath.length() < 1
				|| null == destUri || destUri.length() < 1) {
			return false;
		}

		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(fromPath);

			// check output uri
			String path = null;
			Uri uri = null;

			String lwUri = destUri.toLowerCase();
			if (lwUri.startsWith("content://")) {
				uri = Uri.parse(destUri);
			} else if (lwUri.startsWith("file://")) {
				uri = Uri.parse(destUri);
				path = uri.getPath();
			} else {
				path = destUri;
			}

			// open output
			if (null != path) {
				File fl = new File(path);
				String pth = path.substring(0, path.lastIndexOf("/"));
				File pf = new File(pth);

				if (pf.exists() && !pf.isDirectory()) {
					pf.delete();
				}

//				pf = new File(pth + File.separator);

//				if (!pf.exists()) {
//					if (!pf.mkdirs()) {
//					}
//				}

				pf = new File(path);
				if (pf.exists()) {
					if (pf.isDirectory())
						deleteDirectory(path);
					else
						pf.delete();
				}

				os = new FileOutputStream(path);
				fl.setLastModified(System.currentTimeMillis());
			} else {
				os = new ParcelFileDescriptor.AutoCloseOutputStream(
						cr.openFileDescriptor(uri, "w"));
			}

			// copy file
			byte[] dat = new byte[1024];
			int i = is.read(dat);
			while (-1 != i) {
				os.write(dat, 0, i);
				i = is.read(dat);
			}

			is.close();
			is = null;

			os.flush();
			os.close();
			os = null;

			return true;

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if (null != os) {
				try {
					os.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return false;
	}

	private static byte[] readAll(InputStream is) throws Exception {
		ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
		byte[] buf = new byte[1024];
		int c = is.read(buf);
		while (-1 != c) {
			stream.write(buf, 0, c);
			c = is.read(buf);
		}
		stream.flush();
		stream.close();
		return stream.toByteArray();
	}

	public static byte[] readFile(Context ctx, Uri uri) {
		if (null == ctx || null == uri) {

			return null;
		}

		InputStream is = null;
		String scheme = uri.getScheme().toLowerCase();
		if (scheme.equals("file")) {
			is = readFile(uri.getPath());
		}

		try {
			is = ctx.getContentResolver().openInputStream(uri);
			if (null == is) {
				return null;
			}

			byte[] bret = readAll(is);
			is.close();
			is = null;

			return bret;
		} catch (FileNotFoundException fne) {
			fne.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return null;
	}

	public static boolean writeFile(String filePath, byte[] content) {
		if (null == filePath || null == content) {
			return false;
		}

		FileOutputStream fos = null;
		try {
			String pth = filePath.substring(0, filePath.lastIndexOf("/"));
			File pf;
			pf = new File(pth);
			if (pf.exists() && !pf.isDirectory()) {
				pf.delete();
			}
			pf = new File(filePath);
			if (pf.exists()) {
				if (pf.isDirectory())
					FileHelper.deleteDirectory(filePath);
				else
					pf.delete();
			}

			pf = new File(pth + File.separator);
//			if (!pf.exists()) {
//				if (!pf.mkdirs()) {
//				}
//			}

			fos = new FileOutputStream(filePath);
			fos.write(content);
			fos.flush();
			fos.close();
			fos = null;
			pf.setLastModified(System.currentTimeMillis());

			return true;

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return false;
	}

	/************* ZIP file operation ***************/
	public static boolean readZipFile(String zipFileName, StringBuffer crc) {
		try {
			ZipInputStream zis = new ZipInputStream(new FileInputStream(
					zipFileName));
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				long size = entry.getSize();
				crc.append(entry.getCrc()).append(", size: ").append(size);
			}
			zis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public static byte[] readGZipFile(String zipFileName) {
		if (fileIsExist(zipFileName)) {
			try {
				FileInputStream fin = new FileInputStream(zipFileName);
				int size;
				byte[] buffer = new byte[1024];
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				while ((size = fin.read(buffer, 0, buffer.length)) != -1) {
					stream.write(buffer, 0, size);
				}
				return stream.toByteArray();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	public static boolean zipFile(String baseDirName, String fileName,
			String targetFileName) throws IOException {
		if (baseDirName == null || "".equals(baseDirName)) {
			return false;
		}
		File baseDir = new File(baseDirName);
		if (!baseDir.exists() || !baseDir.isDirectory()) {
			return false;
		}

		String baseDirPath = baseDir.getAbsolutePath();
		File targetFile = new File(targetFileName);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				targetFile));
		File file = new File(baseDir, fileName);

		boolean zipResult;
		if (file.isFile()) {
			zipResult = fileToZip(baseDirPath, file, out);
		} else {
			zipResult = dirToZip(baseDirPath, file, out);
		}
		out.close();
		return zipResult;
	}

	public static void unZipFile(String fileName, String unZipDir)
			throws Exception {
		File f = new File(unZipDir);

		if (!f.exists()) {
			f.mkdirs();
		}

		BufferedInputStream is;
		ZipEntry entry;
		ZipFile zipfile = new ZipFile(fileName);
		Enumeration<?> enumeration = zipfile.entries();
		byte data[] = new byte[FILE_BUFFER_SIZE];

		while (enumeration.hasMoreElements()) {
			entry = (ZipEntry) enumeration.nextElement();

			if (entry.isDirectory()) {
				File f1 = new File(unZipDir + "/" + entry.getName());
				if (!f1.exists()) {
					f1.mkdirs();
				}
			} else {
				is = new BufferedInputStream(zipfile.getInputStream(entry));
				int count;
				String name = unZipDir + "/" + entry.getName();
				RandomAccessFile m_randFile;
				File file = new File(name);
				if (file.exists()) {
					file.delete();
				}

				file.createNewFile();
				m_randFile = new RandomAccessFile(file, "rw");
				int begin = 0;

				while ((count = is.read(data, 0, FILE_BUFFER_SIZE)) != -1) {
					try {
						m_randFile.seek(begin);
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					m_randFile.write(data, 0, count);
					begin = begin + count;
				}

				file.delete();
				m_randFile.close();
				is.close();
			}
		}
	}

	private static boolean fileToZip(String baseDirPath, File file,
			ZipOutputStream out) throws IOException {
		FileInputStream in = null;
		ZipEntry entry;

		byte[] buffer = new byte[FILE_BUFFER_SIZE];
		int bytes_read;
		try {
			in = new FileInputStream(file);
			entry = new ZipEntry(getEntryName(baseDirPath, file));
			out.putNextEntry(entry);

			while ((bytes_read = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytes_read);
			}
			out.closeEntry();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (out != null) {
				out.closeEntry();
			}

			if (in != null) {
				in.close();
			}
		}
		return true;
	}

	private static boolean dirToZip(String baseDirPath, File dir,
			ZipOutputStream out) throws IOException {
		if (!dir.isDirectory()) {
			return false;
		}

		File[] files = dir.listFiles();
		if (files.length == 0) {
			ZipEntry entry = new ZipEntry(getEntryName(baseDirPath, dir));

			try {
				out.putNextEntry(entry);
				out.closeEntry();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (File file : files) {
			if (file.isFile()) {
				fileToZip(baseDirPath, file, out);
			} else {
				dirToZip(baseDirPath, file, out);
			}
		}
		return true;
	}

	private static String getEntryName(String baseDirPath, File file) {
		if (!baseDirPath.endsWith(File.separator)) {
			baseDirPath = baseDirPath + File.separator;
		}

		String filePath = file.getAbsolutePath();
		if (file.isDirectory()) {
			filePath = filePath + "/";
		}

		int index = filePath.indexOf(baseDirPath);
		return filePath.substring(index + baseDirPath.length());
	}

	/**
	 * 创建sd卡的目录
	 * 
	 * @param dir d
	 * @return f
	 */
	public static File createSDDir(String appSDPath, String dir) {
		File appSDPathFile = new File(appSDPath);
		if (!appSDPathFile.exists()) {
			appSDPathFile.mkdirs();
		}
		File destFileDir = new File(appSDPathFile + "/" + dir);
		if (!destFileDir.exists()) {
			destFileDir.mkdirs();
		}

		return destFileDir;

	}

	/**
	 * 创建sd卡的目录
	 * 
	 * @param dir dir
	 */
	private static File createSDDir(String dir) {
		String appSDPath = Environment.getExternalStorageDirectory().getPath()
				+ "/clock";
		File appSDPathFile = new File(appSDPath);
		if (!appSDPathFile.exists()) {
			appSDPathFile.mkdirs();
		}
		File destFileDir = new File(appSDPathFile + "/" + dir);
		if (!destFileDir.exists()) {
			destFileDir.mkdirs();
		}

		return destFileDir;

	}

	/**
	 * 把string 保存在文件中
	 * 
	 * @param filename
	 *            文件名称
	 * @param path
	 *            文件路径
	 * @param content
	 *            要写的内容
	 */
	public static void saveString2File(String path, String filename,
			String content) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return;
		}
		String appSDPath = Environment.getExternalStorageDirectory().getPath()
				+ "/clock";
		File filePath = new File(appSDPath + "/" + path);

		if (!filePath.exists()) {
			createSDDir(path);
		}
		File fileAbsolutely = new File(filePath + "/" + filename);
		if (!fileAbsolutely.exists()) {
			try {
				fileAbsolutely.createNewFile();

				FileOutputStream fos = new FileOutputStream(fileAbsolutely);

				fos.write(content.getBytes());
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			fileAbsolutely.delete();
			try {
				fileAbsolutely.createNewFile();

				FileOutputStream fos = new FileOutputStream(fileAbsolutely);

				fos.write(content.getBytes());
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}