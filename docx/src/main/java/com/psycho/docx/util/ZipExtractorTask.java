package com.psycho.docx.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ZipExtractorTask extends AsyncTask<Void, Integer, Long> {
    private final String TAG = "ZipExtractorTask";
    private final File mInput;
    private final File mOutput;
    //	private final ProgressDialog mDialog;
    private int mProgress = 0;
    //	private final Context mContext;
    private boolean mReplaceAll;

    public ZipExtractorTask(String in, String out,
                            boolean replaceAll) {
        super();
        mInput = new File(in);
        mOutput = new File(out);
        Log.e(TAG, "unzip");
        if (!mOutput.exists()) {
            if (!mOutput.mkdirs()) {
                Log.e(TAG,
                        "Failed to make directories:"
                                + mOutput.getAbsolutePath());
            }
        }
//		if (context != null) {
//			mDialog = new ProgressDialog(context);
//		} else {
//			mDialog = null;
//		}
//		mContext = context;
        mReplaceAll = replaceAll;
    }

    @Override
    protected Long doInBackground(Void... params) {
        // TODO Auto-generated method stub
        return unzip();
    }

    @Override
    protected void onPostExecute(Long result) {
        // TODO Auto-generated method stub
        // super.onPostExecute(result);
//		if (mDialog != null && mDialog.isShowing()) {
//			mDialog.dismiss();
        // Toast.makeText(getApplicationContext(), "网络不可用", 0).show();
//		}
        if (isCancelled())
            return;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        // super.onPreExecute();
//		if (mDialog != null) {
//			mDialog.setTitle("Extracting");
//			mDialog.setMessage(mInput.getName());
//			mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//			mDialog.setOnCancelListener(new OnCancelListener() {
//
//				@Override
//				public void onCancel(DialogInterface dialog) {
//					// TODO Auto-generated method stub
//					cancel(true);
//				}
//			});
//			mDialog.show();
//		}

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // TODO Auto-generated method stub
        // super.onProgressUpdate(values);
//		if (mDialog == null)
//			return;
//		if (values.length > 1) {
//			int max = values[1];
//			mDialog.setMax(max);
//		} else
//			mDialog.setProgress(values[0].intValue());
    }

    private long unzip() {
        Log.e(TAG, "unzip");
        long extractedSize = 0L;
        Enumeration<ZipEntry> entries;
        ZipFile zip = null;
        try {
            zip = new ZipFile(mInput);
            long uncompressedSize = getOriginalSize(zip);
            publishProgress(0, (int) uncompressedSize);

            entries = (Enumeration<ZipEntry>) zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                File destination = new File(mOutput, entry.getName());
                if (!destination.getParentFile().exists()) {
                    Log.e(TAG, "make="
                            + destination.getParentFile().getAbsolutePath());
                    destination.getParentFile().mkdirs();
                }
                if (destination.exists() && !mReplaceAll) {

                }
                ProgressReportingOutputStream outStream = new ProgressReportingOutputStream(
                        destination);
                extractedSize += copy(zip.getInputStream(entry), outStream);
                outStream.close();
            }
            Log.i("mkdirs", "文件解压成功");
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                zip.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return extractedSize;
    }

    private long getOriginalSize(ZipFile file) {
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) file.entries();
        long originalSize = 0l;
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.getSize() >= 0) {
                originalSize += entry.getSize();
            }
        }
        return originalSize;
    }

    private int copy(InputStream input, OutputStream output) {
        byte[] buffer = new byte[1024 * 8];
        BufferedInputStream in = new BufferedInputStream(input, 1024 * 8);
        BufferedOutputStream out = new BufferedOutputStream(output, 1024 * 8);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, 1024 * 8)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    private final class ProgressReportingOutputStream extends FileOutputStream {
        public ProgressReportingOutputStream(File file)
                throws FileNotFoundException {
            super(file);
        }

        @Override
        public void write(byte[] buffer, int byteOffset, int byteCount)
                throws IOException {
            super.write(buffer, byteOffset, byteCount);
            mProgress += byteCount;
            publishProgress(mProgress);
        }
    }
}