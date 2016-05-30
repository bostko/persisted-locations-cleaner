/*
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
package io.brooklyncentral.persistencecleaner.locations;

import com.google.common.base.Joiner;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationsRemover {
    public static void main(String[] args) {
        String brooklynPersistedStatePath= args.length > 0 ? args[0] : Joiner.on(File.separator).join(
                System.getenv().get("HOME"), "brooklyn", "brooklyn-persisted-state");

        LocationsRemover locationsRemover = new LocationsRemover(brooklynPersistedStatePath);
        locationsRemover.loadAllValidLocations();

//        locationsRemover.removeInvalidLocations();
    }

    private String entitiesPath;
    private String locationsPath;

    private List<String> validLocations;

    LocationsRemover(String brooklynPersistedStatePath) {
        entitiesPath = Joiner.on(File.separator).join("data", "entities");
        locationsPath = Joiner.on(File.separator).join("data", "locations");
        validLocations = new ArrayList<String>();
    }

    public void loadAllValidLocations() {
        for (File entityXmlFile : new File(entitiesPath).listFiles()) {
            loadValidLocations(entityXmlFile);
        }
    }

    protected void loadValidLocations(File entityXmlFile) {
        SAXBuilder builder = new SAXBuilder();
        try {

            Document document = (Document) builder.build(entityXmlFile);
            Element rootNode = document.getRootElement();
            List list = rootNode.getChildren("locations");

            for (int i = 0; i < list.size(); i++) {

                Element node = (Element) list.get(i);
                String validLocation = node.getChildText("string");
                System.out.println("Loc 1 : " + node.getChildText("string"));
                validLocations.add(validLocation);
            }

        } catch (IOException io) {
            System.out.println(io.getMessage());
        } catch (JDOMException jdomex) {
            System.out.println(jdomex.getMessage());
        }
    }
}
