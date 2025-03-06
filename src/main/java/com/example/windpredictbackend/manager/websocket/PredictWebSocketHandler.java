package com.example.windpredictbackend.manager.websocket;

import cn.hutool.json.JSONUtil;
import com.example.windpredictbackend.manager.websocket.model.PredictRequestMessage;
import com.example.windpredictbackend.manager.websocket.model.PredictResponseMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author Michael
 */
public class PredictWebSocketHandler extends TextWebSocketHandler {

    /**
     * 实时处理预测数据
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        PredictRequestMessage requestMessage= JSONUtil.toBean(message.getPayload(),PredictRequestMessage.class);
        session.sendMessage(new TextMessage(JSONUtil.toJsonStr(new PredictResponseMessage())));
    }
}
