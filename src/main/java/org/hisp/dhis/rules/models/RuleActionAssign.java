package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class RuleActionAssign
    extends RuleActionData
{

    @Nonnull
    public static RuleActionAssign create( @Nullable String content,
        @Nonnull String data, @Nullable String field )
    {
        if ( content == null && field == null )
        {
            throw new IllegalArgumentException( "Either content or field " +
                "parameters must be not null." );
        }

        return new AutoValue_RuleActionAssign( data, content == null ? "" : content, field == null ? "" : field );
    }

    @Nonnull
    public abstract String content();

    @Nonnull
    public abstract String field();
}
