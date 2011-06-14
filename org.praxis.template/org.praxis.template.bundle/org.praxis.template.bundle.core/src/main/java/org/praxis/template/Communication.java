package org.praxis.template;

import java.util.Date;

/**
 * A Communication is the message being sent by a Sender to a Recipient.
 * @author Jason Rose
 * 
 */
public interface Communication {

  Sender getSender();

  Recipient getRecipient();

  String getMessage();

  String getSubject();

  Date getSendDate();
}
