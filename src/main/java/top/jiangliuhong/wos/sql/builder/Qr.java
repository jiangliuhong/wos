/*
 * Copyright 2020 top.jiangliuhong.wos.sql
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.jiangliuhong.wos.sql.builder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import top.jiangliuhong.wos.sql.api.Routable;
import top.jiangliuhong.wos.sql.builder.internal.Bb;
import top.jiangliuhong.wos.sql.builder.internal.CondQ;
import top.jiangliuhong.wos.sql.mapping.Mappable;
import top.jiangliuhong.wos.sql.parser.Parsed;
import top.jiangliuhong.wos.sql.parser.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@Setter
public final class Qr<T>  implements Mappable, CondQ, Routable {

    private List<Bb> refreshList = new ArrayList<>();
    private String sourceScript;
    private List<Bb> bbs = new ArrayList<>();
    private boolean isAbort;
    private Object routeKey;
    private transient int limit;
    @JsonIgnore
    private transient Class clz;
    @JsonIgnore
    private transient List<Object> valueList = new ArrayList<>();
    @JsonIgnore
    private transient Map<String,String> aliaMap = new HashMap<>();


    public boolean isAbort() {
        return isAbort;
    }

    public void setAbort(boolean abort) {
        isAbort = abort;
    }

    @Override
    public List<Bb> getBbs() {
        return bbs;
    }

    @Override
    public Map<String, String> getAliaMap() {
        return this.aliaMap;
    }
    @Override
    public Map<String,String> getResultKeyAliaMap() {return null;}

    @Override
    public Parsed getParsed() {
        if (this.clz == null)
            return null;
        return Parser.get(this.clz);
    }

    @Override
    public Object getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(Object routeKey) {
        this.routeKey = routeKey;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }


    public KV tryToGetKeyOne() {
        if (clz == null)
            return null;
        Parsed parsed = Parser.get(clz);
        String keyOne = parsed.getKey();
        for (Bb bb : bbs) {
            String key = bb.getKey();
            if (key != null && key.equals(keyOne)) {
                return KV.of(key, bb.getValue());
            }
        }
        return null;
    }


}
