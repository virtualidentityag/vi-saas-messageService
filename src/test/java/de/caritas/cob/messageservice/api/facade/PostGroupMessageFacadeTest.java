package de.caritas.cob.messageservice.api.facade;

import static de.caritas.cob.messageservice.testHelper.TestConstants.MESSAGE_DTO_WITHOUT_NOTIFICATION;
import static de.caritas.cob.messageservice.testHelper.TestConstants.MESSAGE_DTO_WITH_NOTIFICATION;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.caritas.cob.messageservice.api.exception.CustomCryptoException;
import de.caritas.cob.messageservice.api.exception.InternalServerErrorException;
import de.caritas.cob.messageservice.api.exception.RocketChatPostMessageException;
import de.caritas.cob.messageservice.api.model.rocket.chat.message.PostMessageResponseDTO;
import de.caritas.cob.messageservice.api.service.RocketChatService;
import java.util.Date;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PostGroupMessageFacadeTest {

  private final String RC_TOKEN = "r94qMDk8gtgVNzqCq9zD2hELK-eXGB5VHlUVBgE8a8f";
  private final String RC_USER_ID = "pptLwARyTMzbTTRdg";
  private final String RC_GROUP_ID = "fR2Rz7dmWmHdXE8uz";
  private final String RC_FEEDBACK_GROUP_ID = "fR2Rz7dmWmHdXE8uz";
  private final String MESSAGE = "Lorem ipsum";
  private final String CHANNEL = "channel";
  private final PostMessageResponseDTO POST_MESSAGE_RESPONSE_DTO =
      new PostMessageResponseDTO(new Date(), CHANNEL, true, "", "");
  private final PostMessageResponseDTO POST_MESSAGE_RESPONSE_DTO_UNSUCCESSFUL =
      new PostMessageResponseDTO(new Date(), CHANNEL, false, "", "");

  @InjectMocks
  private PostGroupMessageFacade postGroupMessageFacade;
  @Mock
  private RocketChatService rocketChatService;
  @Mock
  private EmailNotificationFacade emailNotificationFacade;


  /**
   * 
   * 
   * Tests for method: postGroupMessage
   *
   *
   */

  @Test(expected = InternalServerErrorException.class)
  public void postGroupMessage_Should_ReturnInternalServerErrorAndNotSendNotification_WhenRocketChatPostMessageFails()
      throws CustomCryptoException {

    RocketChatPostMessageException rocketChatPostMessageException =
        new RocketChatPostMessageException(new Exception());

    when(rocketChatService.postGroupMessage(RC_TOKEN, RC_USER_ID, RC_GROUP_ID, MESSAGE, null))
        .thenThrow(rocketChatPostMessageException);

    postGroupMessageFacade.postGroupMessage(RC_TOKEN, RC_USER_ID, RC_GROUP_ID,
        MESSAGE_DTO_WITH_NOTIFICATION);

    verify(emailNotificationFacade, times(0)).sendEmailNotification(RC_GROUP_ID);
  }

  @Test(expected = InternalServerErrorException.class)
  public void postGroupMessage_Should_ReturnInternalServerErrorAndNotSendNotification_WhenRocketChatServiceReturnsEmptyDTO()
      throws CustomCryptoException {

    when(rocketChatService.postGroupMessage(RC_TOKEN, RC_USER_ID, RC_GROUP_ID, MESSAGE, null))
        .thenReturn(null);

    postGroupMessageFacade.postGroupMessage(RC_TOKEN, RC_USER_ID, RC_GROUP_ID,
        MESSAGE_DTO_WITH_NOTIFICATION);

    verify(emailNotificationFacade, times(0)).sendEmailNotification(RC_GROUP_ID);
  }

  @Test(expected = InternalServerErrorException.class)
  public void postGroupMessage_Should_ReturnInternalServerErrorAndNotSendNotification_WhenRocketChatServiceReturnsUnsuccessful()
      throws CustomCryptoException {

    when(rocketChatService.postGroupMessage(RC_TOKEN, RC_USER_ID, RC_GROUP_ID, MESSAGE, null))
        .thenReturn(POST_MESSAGE_RESPONSE_DTO_UNSUCCESSFUL);

    postGroupMessageFacade.postGroupMessage(RC_TOKEN, RC_USER_ID, RC_GROUP_ID,
        MESSAGE_DTO_WITH_NOTIFICATION);

    verify(emailNotificationFacade, times(0)).sendEmailNotification(RC_GROUP_ID);
  }

  @Test(expected = InternalServerErrorException.class)
  public void postGroupMessage_Should_ReturnRocketChatPostMarkGroupAsReadExceptionAndNotSendNotification_WhenRocketChatMarkGroupAsReadFails()
      throws CustomCryptoException {

    when(rocketChatService.postGroupMessage(RC_TOKEN, RC_USER_ID, RC_GROUP_ID, MESSAGE, null))
        .thenReturn(null);

    postGroupMessageFacade.postGroupMessage(RC_TOKEN, RC_USER_ID, RC_GROUP_ID,
        MESSAGE_DTO_WITH_NOTIFICATION);

    verify(emailNotificationFacade, times(0)).sendEmailNotification(RC_GROUP_ID);
  }

  @Test
  public void postGroupMessage_Should_ReturnCreatedAndSendNotification_When_MessageWasSentAndNotificationIsSetToTrue()
      throws CustomCryptoException {

    when(rocketChatService.postGroupMessage(RC_TOKEN, RC_USER_ID, RC_GROUP_ID, MESSAGE, null))
        .thenReturn(POST_MESSAGE_RESPONSE_DTO);

    postGroupMessageFacade.postGroupMessage(RC_TOKEN, RC_USER_ID, RC_GROUP_ID,
        MESSAGE_DTO_WITH_NOTIFICATION);

    verify(emailNotificationFacade, times(1)).sendEmailNotification(RC_GROUP_ID);
  }

  @Test
  public void postGroupMessage_Should_ReturnCreatedAndNotSendNotification_When_MessageWasSentAndNotificationIsSetToFalse()
      throws CustomCryptoException {

    when(rocketChatService.postGroupMessage(RC_TOKEN, RC_USER_ID, RC_GROUP_ID, MESSAGE, null))
        .thenReturn(POST_MESSAGE_RESPONSE_DTO);

    postGroupMessageFacade.postGroupMessage(RC_TOKEN, RC_USER_ID, RC_GROUP_ID,
        MESSAGE_DTO_WITHOUT_NOTIFICATION);

    verify(emailNotificationFacade, times(0)).sendEmailNotification(Mockito.anyString());
  }

  /**
   * 
   * 
   * Tests for method: postFeedbackGroupMessage
   *
   *
   */

  @Test(expected = InternalServerErrorException.class)
  public void postFeebackGroupMessage_Should_ReturnInternalServerErrorAndNotSendNotification_WhenRocketChatPostMessageFails()
      throws CustomCryptoException {

    RocketChatPostMessageException rocketChatPostMessageException =
        new RocketChatPostMessageException(new Exception());

    when(rocketChatService.postGroupMessage(RC_TOKEN, RC_USER_ID, RC_FEEDBACK_GROUP_ID, MESSAGE,
        null)).thenThrow(rocketChatPostMessageException);

    postGroupMessageFacade.postFeedbackGroupMessage(RC_TOKEN, RC_USER_ID,
        RC_FEEDBACK_GROUP_ID, MESSAGE, null);

    verify(emailNotificationFacade, times(0)).sendEmailNotification(RC_GROUP_ID);
  }

  @Test(expected = InternalServerErrorException.class)
  public void postFeedbackGroupMessage_Should_ReturnInternalServerErrorAndNotSendFeedbackNotification_WhenRocketChatServiceReturnsEmptyDTO()
      throws CustomCryptoException {

    when(rocketChatService.postGroupMessage(RC_TOKEN, RC_USER_ID, RC_FEEDBACK_GROUP_ID, MESSAGE,
        null)).thenReturn(null);

    postGroupMessageFacade.postFeedbackGroupMessage(RC_TOKEN, RC_USER_ID,
        RC_FEEDBACK_GROUP_ID, MESSAGE, null);

    verify(emailNotificationFacade, times(0)).sendFeedbackEmailNotification(RC_FEEDBACK_GROUP_ID);
  }

  @Test(expected = InternalServerErrorException.class)
  public void postFeedbackGroupMessage_Should_ReturnInternalServerErrorAndNotSendNotification_WhenRocketChatServiceReturnsUnsuccessful()
      throws CustomCryptoException {

    when(rocketChatService.postGroupMessage(RC_TOKEN, RC_USER_ID, RC_GROUP_ID, MESSAGE, null))
        .thenReturn(POST_MESSAGE_RESPONSE_DTO_UNSUCCESSFUL);

    postGroupMessageFacade.postFeedbackGroupMessage(RC_TOKEN, RC_USER_ID,
        RC_FEEDBACK_GROUP_ID, MESSAGE, null);

    verify(emailNotificationFacade, times(0)).sendEmailNotification(RC_FEEDBACK_GROUP_ID);
  }

  @Test(expected = InternalServerErrorException.class)
  public void postFeedbackGroupMessage_Should_ReturnRocketChatPostMarkGroupAsReadExceptionAndNotSendFeedbackNotification_WhenRocketChatMarkGroupAsReadFails()
      throws CustomCryptoException {

    when(rocketChatService.postGroupMessage(RC_TOKEN, RC_USER_ID, RC_FEEDBACK_GROUP_ID, MESSAGE,
        null)).thenReturn(null);

    postGroupMessageFacade.postFeedbackGroupMessage(RC_TOKEN, RC_USER_ID,
        RC_FEEDBACK_GROUP_ID, MESSAGE, null);

    verify(emailNotificationFacade, times(0)).sendFeedbackEmailNotification(RC_FEEDBACK_GROUP_ID);
  }

  @Test
  public void postFeedbackGroupMessage_Should_ReturnCreatedAndSendFeedbackNotification_WhenRocketChatServiceSucceeds()
      throws CustomCryptoException {

    when(rocketChatService.postGroupMessage(RC_TOKEN, RC_USER_ID, RC_FEEDBACK_GROUP_ID, MESSAGE,
        null)).thenReturn(POST_MESSAGE_RESPONSE_DTO);

    postGroupMessageFacade.postFeedbackGroupMessage(RC_TOKEN, RC_USER_ID,
        RC_FEEDBACK_GROUP_ID, MESSAGE, null);

    verify(emailNotificationFacade, times(1)).sendFeedbackEmailNotification(RC_FEEDBACK_GROUP_ID);
  }

}