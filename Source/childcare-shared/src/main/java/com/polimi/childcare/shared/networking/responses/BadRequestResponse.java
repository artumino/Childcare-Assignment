package com.polimi.childcare.shared.networking.responses;

public class BadRequestResponse extends BaseResponse
{
    public BadRequestResponse()
    {
        super(403);
    }

    public static class BadRequestResponseWithMessage extends BadRequestResponse
    {
        private String message;

        public BadRequestResponseWithMessage(String message) {
            super();
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
