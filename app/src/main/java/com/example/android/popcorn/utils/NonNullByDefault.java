package com.example.android.popcorn.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.annotation.meta.TypeQualifierDefault;


/*
 * For the poor developer, looking at this piece of code.
 * https://metabroadcast.com/blog/getting-intellij-to-dislike-nulls-as-much-as-the-rest-of-us
 */
@Documented
@android.support.annotation.Nullable
@TypeQualifierDefault(
        {
                ElementType.ANNOTATION_TYPE,
                ElementType.CONSTRUCTOR,
                ElementType.FIELD,
                /*ElementType.LOCAL_VARIABLE, // remove for readability*/
                ElementType.METHOD,
                ElementType.PACKAGE,
                ElementType.PARAMETER,
                ElementType.TYPE
        })
@Retention(RetentionPolicy.SOURCE)
public @interface NonNullByDefault {

}