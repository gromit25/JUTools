package com.jutools.publish.formatter.flow;

import com.jutools.publish.formatter.FormatterSpec;

/**
 * default formatter
 * switch 분기시, default 분기를 수행함 flow를 수행함
 * BasicFlowFormatter와 동일함
 * 
 * @author jmsohn
 */
@FormatterSpec(group="flow", tag="default")
public class DefaultFormatter extends AbstractFlowComponentFormatter {
}
