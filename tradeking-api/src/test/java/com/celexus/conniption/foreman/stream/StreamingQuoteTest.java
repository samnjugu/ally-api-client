/**
 * Copyright 2013 Cameron Cook
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.celexus.conniption.foreman.stream;

import java.util.List;
import java.util.concurrent.Future;

import org.junit.Test;

import com.celexus.conniption.model.quotes.Quote;
public class StreamingQuoteTest {

	@Test
	//@Ignore
	public void test() throws Exception {
		StreamingQuote quote = new StreamingQuote();

		/**
		 * Implement the StreamHandler interface to be used on the spot.
		 */
		StreamHandler<Quote> handler = new StreamHandler<Quote>() {
			public void handle(Quote quote) {
				System.out.println(quote.toString());
			}
		};

		Future<List<Quote>> future = quote.stream(handler, "AAPL", "TVIX");
		System.out.println("Quotes before close: " + future.get().size());
	}
}
