/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.terasology.rendering.nui.internal;

import java.util.Collection;

import org.terasology.context.Context;
import org.terasology.i18n.TranslationSystem;
import org.terasology.reflection.metadata.ClassLibrary;
import org.terasology.reflection.metadata.ClassMetadata;
import org.terasology.reflection.metadata.FieldMetadata;
import org.terasology.rendering.nui.UIWidget;

/**
 * Translates all UI elements that contain a string element that contains <code>${module:project#id}</code>.
 */
public class NUITranslator {

    private final Context context;
    private final ClassLibrary<UIWidget> classLibrary;

    public NUITranslator(Context context, ClassLibrary<UIWidget> classLibrary) {
        this.context = context;
        this.classLibrary = classLibrary;
    }

    public void updateWidget(UIWidget container) {
        TranslationSystem system = context.get(TranslationSystem.class);
        Collection<UIWidget> elements = container.findAll(UIWidget.class);

        for (UIWidget element : elements) {
            ClassMetadata<UIWidget, ?> metadata = classLibrary.getMetadata(element);
            if (metadata != null) {
                for (FieldMetadata<? extends UIWidget, ?> field : metadata.getFields()) {
                    if (field.getType().equals(String.class)) {
                        String value = (String) field.getValue(element);
                        String i18nId = extractId(value);
                        if (i18nId != null) {
                            String i18nText = system.translate(i18nId);
                            field.setValue(element, i18nText);
                        }
                    }
                }
            }
        }
    }

    private static String extractId(String text) {
        if (text != null) {
            int start = text.indexOf("${");
            int end = text.lastIndexOf('}');
            // TODO: maybe check for ":" and "#" in between - use regex?
            if (start >= 0 && end > start) {
                return text.substring(start + 2, end);
            }
        }
        return null;
    }
}