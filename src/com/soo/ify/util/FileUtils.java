/*
 * Copyright (c) 2013-2014 Soo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soo.ify.util;

import java.io.File;
import java.io.IOException;

import android.os.StatFs;

/**
 * @author Soo
 *
 */
public class FileUtils {

    public static boolean clearFile(File file) {
        if (!file.exists()) {
            return false;
        }
        if (!file.isDirectory()) {
            return file.delete();
        }
        File[] files = file.listFiles();
        for (File child : files) {
            clearFile(child);
        }
        return true;
    }

    public static File createDir(String dir, boolean flag) {
        if (dir != null && dir.trim().length() > 0) {
            if (!dir.endsWith(File.separator)) {
                dir += File.separator;
            }

            File file = new File(dir);
            if (file.exists()) {
                if (flag) {
                    file.delete();
                } else {
                    return file;
                }
            }
            if (file.mkdirs()) {
                return file;
            }
        }
        return null;
    }

    /**
     * 根据目录与文件名创建一个文件，注意：flag为true总是返回一个全新的文件，flag为false 表示如果没有该文件就创建一个新的，有就直接返回
     */
    public static File createFile(String filePath, boolean flag) {
        File file = new File(filePath);
        String directory = file.getParent();
        String name = file.getName();
        return createFile(directory, name, flag);
    }

    /**
     * 根据目录与文件名创建一个文件，注意：flag为true总是返回一个全新的文件，flag为false 表示如果没有该文件就创建一个新的，有就直接返回
     */
    public static File createFile(File file, String name, boolean flag) {
        if (file != null && file.isDirectory()) {
            File newFile = new File(file, name);
            return createFile(newFile.getAbsolutePath(), flag);
        }
        return null;
    }

    /**
     * 根据目录与文件名创建一个文件，注意：flag为true总是返回一个全新的文件，flag为false 表示如果没有该文件就创建一个新的，有就直接返回
     */
    public static File createFile(String directory, String name, boolean flag) {
        File file = null;
        if (directory != null && directory.trim().length() > 0 && name != null
                && name.trim().length() > 0) {
            File dirFile = new File(directory);
            dirFile.mkdirs();
            file = new File(dirFile, name);
            if (flag)
                file.delete();
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 获取文件的最大剩余空间
     */
    public static long getFileMaxAvaliableSapce(File file) {
        return getFileMaxAvaliableSapce(file.getPath());
    }

    /**
     * 获取文件的最大剩余空间
     */
    public static long getFileMaxAvaliableSapce(String path) {
        final StatFs stats = new StatFs(path);
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }
}
