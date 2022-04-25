package com.example.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */

@JsonPropertyOrder(value = {
		"data",
		"totalRecord",
		"page",
		"size",
		"totalPage"
})
@Data
@AllArgsConstructor
public class CommonResponse implements Serializable {
	private Object[] data;
	private int totalRecord;
	private int page;
	private int size;
	private int totalPage;

	public CommonResponse() {}
	public CommonResponse getCommonResponse(int page, int size, List result){
		int offset = (page - 1) * size;
		int total = result.size();
		int totalPage = (total % size) == 0 ? (int)(total / size) : (int)((total / size) + 1);
		Object[] data = result.stream().skip(offset).limit(size).toArray();
		return new CommonResponse(data, total, page, size, totalPage);
	}
}
