/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.jboss.netty.example.telnet;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.ssl.SslContext;

import static org.jboss.netty.channel.Channels.*;

/**
 * Creates a newly configured {@link ChannelPipeline} for a new channel.
 */
public class TelnetServerPipelineFactory implements ChannelPipelineFactory {

    private final SslContext sslCtx;

    public TelnetServerPipelineFactory(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    public ChannelPipeline getPipeline() {
        // Create a default pipeline implementation.
        ChannelPipeline pipeline = pipeline();

        if (sslCtx != null) {
            pipeline.addLast("ssl", sslCtx.newHandler());
        }

        // Add the text line codec combination first,
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(
                8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());

        // and then business logic.
        pipeline.addLast("handler", new TelnetServerHandler());

        return pipeline;
    }
}
