package controller;

import com.swlc.social_media.controller.LoginController;
import com.swlc.social_media.controller.SubscriberController;
import com.swlc.social_media.dto.ChannelDTO;
import com.swlc.social_media.model.ChannelModel;
import com.swlc.social_media.entity.ChannelEntity;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import static org.mockito.Mockito.*;
public class SubscriberControllerTest extends ApplicationTest {

    private SubscriberController controller;

    private ChannelModel mockChannelModel;

    @Test
    void testSubscribeButton() {
        controller = new SubscriberController();
        mockChannelModel = Mockito.mock(ChannelModel.class);
        // Mock logged channel and subscription behavior
        LoginController.currentLoggedChannel= new ChannelEntity("99L", "Logged Channel", "user.png");
        doNothing().when(mockChannelModel).subscribeChannel(any(), any());

        // Add a post with "subscribe" action
        controller.subBtnAction = "sub";
        interact(() -> controller.addPost("Channel A", "logoA.png", 123L));

        // Simulate button click
        Button subscribeButton = (Button) ((VBox) controller.postPnl.getChildren().get(0)).getChildren().get(2);
        interact(subscribeButton::fire);

        // Verify subscription logic
        verify(mockChannelModel, times(1))
                .subscribeChannel(eq(new ChannelDTO(123L, "Channel A")), eq(new ChannelDTO(123L, "Channel A")));
    }

    @Test
    void testUnsubscribeButton() {
        // Mock logged channel and unsubscription behavior
        LoginController.currentLoggedChannel = new ChannelEntity("1", "Logged Channel", "user.png");
        doNothing().when(mockChannelModel).unSubscribeChannel(any(), any());

        // Add a post with "unsubscribe" action
        controller.subBtnAction = "unsub";
        interact(() -> controller.addPost("Channel B", "logoB.png", 456L));

        // Simulate button click
        Button unsubscribeButton = (Button) ((VBox) controller.postPnl.getChildren().get(0)).getChildren().get(2);
        interact(unsubscribeButton::fire);

        // Verify unsubscription logic
        verify(mockChannelModel, times(1))
                .unSubscribeChannel(eq(new ChannelDTO(456L, "Channel B")), eq(new ChannelDTO(456L, "Channel B")));
    }
}
