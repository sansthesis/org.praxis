package org.praxis.template;

/**
 * A Recipient is the intended recipient of a rendered Template.
 * @author Jason Rose
 * 
 */
public interface Recipient {

  String getFirstName();

  String getLastName();

  String getEmail();
}
