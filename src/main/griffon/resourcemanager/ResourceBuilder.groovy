package griffon.resourcemanager

/*
 * Copyright 2010 the original author or authors.
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

/**
 * @author Alexander Klein
 */

class ResourceBuilder extends FactoryBuilderSupport {
    public ResourceBuilder(boolean init = true) {
        super(init)
    }

    public void registerResources() {
        registerFactory 'url', new URLFactory()
        registerFactory 'uri', new URIFactory()
    }
}

class URLFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (!value)
            throw new RuntimeException("The URL-spec must be provided");
        if (attributes.context instanceof URL)
            return new URL(attributes.context, value)
        else
            return new URL(value)
    }

    @Override
    boolean isLeaf() { true }
}

class URIFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (!value)
            throw new RuntimeException("The URI-spec must be provided");
        else
            return new URI(value)
    }

    @Override
    boolean isLeaf() { true }
}

// TODO: Color, Dimension, Font, Image, Icon, Insets, Point, Rectangle, Locale

