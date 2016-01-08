/**
 * <copyright>
 *
 * Copyright (c) 2015 PlugBee. All rights reserved.
 * 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which 
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Amine Lajmi - Initial API and implementation
 *
 * </copyright>
 */
package org.dslforge.workspace.ui;

import java.util.Set;

import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreePathViewerSorter;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.internal.navigator.CommonNavigatorMessages;
import org.eclipse.ui.internal.navigator.NavigatorContentService;
import org.eclipse.ui.internal.navigator.NavigatorContentServiceContentProvider;
import org.eclipse.ui.internal.navigator.NavigatorPlugin;
import org.eclipse.ui.internal.navigator.Policy;
import org.eclipse.ui.navigator.INavigatorContentDescriptor;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.eclipse.ui.navigator.INavigatorSorterService;
import org.eclipse.ui.navigator.Priority;

@SuppressWarnings({ "serial", "restriction" })
public class BasicWorkspaceSorter extends TreePathViewerSorter {

	private static final int LEFT_UNDERSTANDS = 1;
	private static final int RIGHT_UNDERSTANDS = 2; 
	private static final int BOTH_UNDERSTAND = LEFT_UNDERSTANDS | RIGHT_UNDERSTANDS; 

	private NavigatorContentService contentService;

	private INavigatorSorterService sorterService;

	/**
	 * Create a sorter service attached to the given content service.
	 * 
	 * @param aContentService
	 *            The content service used by the viewer that will use this sorter service.
	 * @since 3.3
	 */
	public void setContentService(INavigatorContentService aContentService) {
		contentService = (NavigatorContentService) aContentService;
		sorterService = contentService.getSorterService();
	}

	public int category(Object element) {
		if (contentService == null)
			return 0;
		INavigatorContentDescriptor source = getSource(element);
		return source != null ? source.getSequenceNumber() : Priority.NORMAL_PRIORITY_VALUE;
	}

	private void logMissingExtension(Object parent, Object object) {
		NavigatorPlugin.logError(0, NLS.bind(CommonNavigatorMessages.CommonViewerSorter_NoContentExtensionForObject,
				object != null ? object.toString() : "<null>", parent != null ? parent.toString() : "<null>"), null); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public int compare(Viewer viewer, TreePath parentPath, Object e1, Object e2) {
		if (contentService == null)
			return -1;
		INavigatorContentDescriptor sourceOfLvalue = getSource(e1);
		INavigatorContentDescriptor sourceOfRvalue = getSource(e2);
		
		Object parent;
		if (parentPath == null) {
			parent = viewer.getInput();
		} else {
			parent = parentPath.getLastSegment();
		}
		
		if (sourceOfLvalue == null) {
			logMissingExtension(parent, e1);
			return -1;
		}
		if (sourceOfRvalue == null) {
			logMissingExtension(parent, e2);
			return -1;
		}

		ViewerSorter sorter = null;

		// shortcut if contributed by same source
		if (sourceOfLvalue == sourceOfRvalue) {
			sorter = sorterService.findSorter(sourceOfLvalue, parent, e1, e2);
		} else {

			boolean flags[] = new boolean[4];
			flags[0] = sourceOfLvalue.isTriggerPoint(e1);
			flags[1] = sourceOfLvalue.isTriggerPoint(e2);
			flags[2] = sourceOfRvalue.isTriggerPoint(e1);
			flags[3] = sourceOfRvalue.isTriggerPoint(e2);

			int whoknows = 0;
			whoknows = whoknows | (flags[0] & flags[1] ? LEFT_UNDERSTANDS : 0);
			whoknows = whoknows | (flags[2] & flags[3] ? RIGHT_UNDERSTANDS : 0);

			switch (whoknows) {
			case BOTH_UNDERSTAND:
				sorter = sourceOfLvalue.getSequenceNumber() > sourceOfRvalue.getSequenceNumber() ? sorterService
						.findSorter(sourceOfLvalue, parent, e1, e2)
						: sorterService.findSorter(sourceOfRvalue, parent, e1, e2);
				break;
			case LEFT_UNDERSTANDS:
				sorter = sorterService.findSorter(sourceOfLvalue, parent, e1, e2);
				break;
			case RIGHT_UNDERSTANDS:
				sorter = sorterService.findSorter(sourceOfRvalue, parent, e1, e2);
				break;
			}
		}
		
		if (sorter != null) {
			return sorter.compare(viewer, e1, e2);
		}
 
		int categoryDelta = category(e1) - category(e2);
		if (categoryDelta == 0) {
			return super.compare(viewer, e1, e2);
		}
		return categoryDelta;
	}

    public boolean isSorterProperty(Object element, String property) {
    	// Have to get the parent path from the content provider
    	NavigatorContentServiceContentProvider cp = (NavigatorContentServiceContentProvider) contentService.createCommonContentProvider();
    	TreePath[] parentPaths = cp.getParents(element);
    	for (int i = 0; i < parentPaths.length; i++) {
    		if (isSorterProperty(parentPaths[i], element, property))
    			return true;
    	}
    	return false;
    }

    public boolean isSorterProperty(TreePath parentPath, Object element, String property) {
		INavigatorContentDescriptor contentDesc = getSource(element);
		if (parentPath.getSegmentCount() == 0)
			return false;
		ViewerSorter sorter = sorterService.findSorter(contentDesc, parentPath.getLastSegment(), element, null);
		if (sorter != null)
			return sorter.isSorterProperty(element, property);
        return false;
    }

    
	private INavigatorContentDescriptor getSource(Object o) {
		INavigatorContentDescriptor ncd = contentService.getSourceOfContribution(o);
		if (ncd != null) {
			if (Policy.DEBUG_SORT)
				System.out.println("sort: " + ncd + " object: " + o); //$NON-NLS-1$//$NON-NLS-2$
			return ncd;
		}

		Set<?> descriptors = contentService.findDescriptorsByTriggerPoint(o, NavigatorContentService.CONSIDER_OVERRIDES);
		if (descriptors != null && descriptors.size() > 0) {
			ncd = (INavigatorContentDescriptor) descriptors.iterator().next();
			if (Policy.DEBUG_SORT)
				System.out.println("sort: " + ncd + " object: " + o); //$NON-NLS-1$//$NON-NLS-2$
			return ncd;
		}
		if (Policy.DEBUG_SORT)
			System.out.println("sort: NULL object: " + o); //$NON-NLS-1$
		return null;
	}
}