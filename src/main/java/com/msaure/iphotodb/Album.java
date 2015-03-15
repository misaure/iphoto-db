/*
 * Copyright 2015 msaure.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.msaure.iphotodb;

import java.util.Set;

public class Album implements java.io.Serializable {
    
    private static final long serialVersionUID = -4866205658942134638L;
    
    public static enum Type {
        EVENT, MASTER, FLAGGED, SPECIAL_MONTH, UNKNWON, SLIDESHOW;
    }
    
    private int id;
    private Type type;
    private String name;
    private Set<Integer> imageIds;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Integer> getImageIds() {
        return imageIds;
    }

    public void setImageIds(Set<Integer> imageIds) {
        this.imageIds = imageIds;
    }

    @Override
    public String toString() {
        return "Album{" + "id=" + id + ", type=" + type + ", name=" + name + ", imageIds=" + imageIds + '}';
    }
    
}
