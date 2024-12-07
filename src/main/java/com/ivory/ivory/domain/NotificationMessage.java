package com.ivory.ivory.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class NotificationMessage {
    private String message;
    private String childName;
    private String startDate;

}
