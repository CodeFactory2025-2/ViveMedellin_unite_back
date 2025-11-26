package com.communityapp.group.request;

import jakarta.validation.constraints.NotNull;
public class JoinRequestDto {

    @NotNull(message = "groupId es requerido")
    private Long groupId;

    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }
    
}

