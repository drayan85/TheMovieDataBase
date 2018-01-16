/*
 * Copyright (c) 2018 Ilanthirayan Paramanathan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.database.movie.data_layer.api.response;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 15th of January 2018
 */
public class APIError {

    private int status_code;//not HTTP header response code, it's custom status_code code from response body
    private String status_message;// custom status_message from HTTP Response Body
    private String error;//custom error status_message from HTTP Response Body

    public int getStatus_code() {
        return status_code;
    }

    public String getStatus_message() {
        if(status_message == null || status_message.isEmpty()){
            if(error != null){
                return error;
            }else{
                return " ";
            }
        }else {
            return status_message;
        }
    }

    public String getError() {
        return error;
    }
}
