/*
 * Copyright 2014 - 2018 Michael Rapp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.mrapp.android.dialog.builder;

import android.content.Context;
import android.support.annotation.NonNull;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An abstract base class for all builders.
 *
 * @param <Type>
 *         The type of the class, which is created by the builder
 * @param <BuilderType>
 *         The type of the builder
 * @author Michael Rapp
 * @since 4.0.0
 */
public abstract class AbstractBuilder<Type, BuilderType extends AbstractBuilder<Type, ?>> {

    /**
     * The context, which is used by the builder.
     */
    private Context context;

    /**
     * The class, which is configured by the builder.
     */
    private Type product;

    /**
     * Returns the builder itself, casted to the generic type BuilderType.
     *
     * @return The builder itself as an instance of the generic type BuilderType
     */
    @SuppressWarnings("unchecked")
    protected final BuilderType self() {
        return (BuilderType) this;
    }

    /**
     * Returns the class, which is configured by the builder.
     *
     * @return The class, which is configured by the builder, as an instance of the generic type
     * Type
     */
    protected final Type getProduct() {
        if (product == null) {
            product = onCreateProduct();
        }

        return product;
    }

    /**
     * Sets the context, which should be used by the builder.
     *
     * @param context
     *         The context, which should be set, as an instance of the class {@link Context}. The
     *         context may not be null
     */
    protected final void setContext(@NonNull final Context context) {
        ensureNotNull(context, "The context may not be null");
        this.context = context;
    }

    /**
     * The method, which is invoked on subclasses in order to create the class, which is configured
     * by the builder.
     *
     * @return The class, which has been created, as an instance of the generic type Type. The class
     * may not be null
     */
    @NonNull
    protected abstract Type onCreateProduct();

    /**
     * Creates a new builder.
     *
     * @param context
     *         The context, which should be used by the builder, as an instance of the class {@link
     *         Context}. The context may not be null
     */
    public AbstractBuilder(@NonNull final Context context) {
        setContext(context);
    }

    /**
     * Returns the context, which is used by the builder.
     *
     * @return The context, which is used by the builder, as an instance of the class {@link
     * Context}
     */
    public final Context getContext() {
        return context;
    }

    /**
     * Creates the class, which has been configured by the builder.
     *
     * @return The clas, which has been configured, as an instance of the generic type Type
     */
    public final Type create() {
        return getProduct();
    }

}