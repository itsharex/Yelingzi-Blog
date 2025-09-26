package com.yeling.yelingziblog.websocket;


import com.yeling.yelingziblog.common.utils.JwtUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.QueryStringDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpHeadersHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest request) {

            /* ========== 1. 解析 query 取 token ========== */
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            Map<String, List<String>> parameters = decoder.parameters();
            String token = parameters.getOrDefault("token", List.of()).stream()
                    .findFirst()
                    .orElse("");

            NettyUtil.setAttr(ctx.channel(), NettyUtil.TOKEN, token);

            /* ========== 2. JWT 解析 userId ========== */
            Integer userId;
            try {
                userId = JwtUtils.getUserId(token);
            } catch (Exception e) {
                userId = 0;
            }
            NettyUtil.setAttr(ctx.channel(), NettyUtil.UserID, userId);

            /* ========== 3. 去掉 query，只保留 path ========== */
            String cleanPath = decoder.path();
            request.setUri(cleanPath);

            /* ========== 4. 获取真实 IP ========== */
            HttpHeaders headers = request.headers();
            String ip = headers.get("X-Real-IP");
            if (!StringUtils.hasText(ip)) {          // 未经过 nginx
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                ip = address.getAddress().getHostAddress();
            }
            NettyUtil.setAttr(ctx.channel(), NettyUtil.IP, ip);

            /* ========== 5. 把自己从 pipeline 摘掉，继续传播 ========== */
            ctx.pipeline().remove(this);
            ctx.fireChannelRead(request);
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}