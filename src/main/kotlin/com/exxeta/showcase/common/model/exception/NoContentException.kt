package com.exxeta.showcase.common.model.exception

import javax.ws.rs.ClientErrorException
import javax.ws.rs.core.Response

class NoContentException(message: String) : ClientErrorException(message, Response.Status.NO_CONTENT)
