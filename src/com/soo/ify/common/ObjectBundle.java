/*
 * Copyright 2015 Soo [154014022@qq.com | sootracker@gmail.com]
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */

package com.soo.ify.common;

/**
 * @author Soo.
 */
public class ObjectBundle {
    private static final String TAG = "ObjectBundle--->";

    public static ObjectBundle bundle(Object...objects) {
        return new ObjectBundle(objects);
    }

    private Object[] objects;

    private ObjectBundle(Object...objects) {
        this.objects = objects;
    }

    public <T> T get(int position) {
        if (objects == null || position > (objects.length - 1)) {
            return null;
        }
        Object obj = objects[position];
        try {
            T t = (T) obj;
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof ObjectBundle) {
            final Object[] lObjs = objects;
            final Object[] rObjs = ((ObjectBundle) o).objects;
            if (lObjs.length == rObjs.length) {
                int count = lObjs.length;
                for (int i = 0; i < count; i++) {
                    if (!lObjs[i].equals(rObjs[i])) {
                        return false;
                    }
                }
                return true;
            }
        }
        return super.equals(o);
    }
}
