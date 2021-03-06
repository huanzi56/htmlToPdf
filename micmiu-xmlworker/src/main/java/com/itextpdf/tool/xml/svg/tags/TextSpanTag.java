/*
 * $Id: TextSpanTag.java 341 2012-06-18 14:38:54Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2012 1T3XT BVBA
 * Authors: VVB, Bruno Lowagie, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.tool.xml.svg.tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.css.apply.ChunkCssApplier;
import com.itextpdf.tool.xml.html.HTMLUtils;
import com.itextpdf.tool.xml.svg.AbstractGraphicProcessor;
import com.itextpdf.tool.xml.svg.graphic.Text;


public class TextSpanTag extends AbstractGraphicProcessor {
	final static String X = "x";
	final static String Y = "y";
	final static String DX = "dx";
	final static String DY = "dy";
	
	@Override	
	public List<Element> start(WorkerContext ctx, Tag tag) {
		return new ArrayList<Element>(0);
	}
	
	@Override
	public List<Element> content(WorkerContext ctx, Tag tag, String content) {
		
		float x = -1,  y = -1;
		
		Map<String, String> attributes = tag.getAttributes();
		if(attributes != null){			
			try{
				x = Integer.parseInt(attributes.get(X));
			}catch (Exception e) {
				// TODO: handle exception
			}
			
			try{
				y = Integer.parseInt(attributes.get(Y));
			}catch (Exception e) {
				// TODO: handle exception
			}
		}		
		
    	List<Element> l = new ArrayList<Element>(1);
		String sanitized = content.trim(); //TODO check this
		
    	if (sanitized.length() > 0) {    	
    		Chunk c = new ChunkCssApplier().apply(new Chunk(sanitized), tag);
		    if(x == -1 && y == -1){
		    	l.add(new Text(c, tag.getCSS(), split(attributes.get(DX)), split(attributes.get(DY))));
		    }else{
		    	l.add(new Text(c, x, y, tag.getCSS(), split(attributes.get(DX)), split(attributes.get(DY))));
		    }
    	}
	    return l;
	}
	
	private List<Integer> split(String str){
		List<Integer> result = new ArrayList<Integer>();
		if(str == null) return null;
		
		List<String> list = TagUtils.splitValueList(str);
		for (int i = 0; i < list.size(); i++) {
			try{
				result.add(new Integer(list.get(i)));
			}catch(NumberFormatException exp){
				//TODO, check what 
			}
		}
		return result;
	}	
		
	@Override
	public List<Element> end(WorkerContext ctx, Tag tag,
				List<Element> currentContent) {

		return currentContent;
	}
	
	@Override
	public boolean isStackOwner() {
		return true;
	}
}

