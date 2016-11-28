package io.happyxml.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 标注方法是初始化方法，对象被构建后该方法会被执行
 */
@Retention(RUNTIME) @Target({METHOD})
public @interface XmlInit {
	
}
