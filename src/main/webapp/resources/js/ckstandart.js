/**
 * @license Copyright (c) 2003-2013, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	config.language = 'tr';
	// config.uiColor = '#AADC6E';
    // config.toolbar= 'Basic';
    config.filebrowserBrowseUrl= 'admin/uploadFile.html'
    //config.filebrowserUploadUrl= 'uploader/upload.php?type=Files';
  config.toolbarGroups = [
        {name: 'clipboard', groups: ['clipboard', 'undo']},
        {name: 'editing', groups: ['find', 'selection', 'spellchecker']},
        {name: 'links'},
        {name: 'insert'},
        {name: 'tools'},
        {name: 'document', groups: ['mode', 'document']},
        {name: 'colors'},
        {name: 'basicstyles', groups: ['basicstyles', 'cleanup']},
        {name: 'paragraph', groups: ['list', 'indent', 'blocks', 'align']},
        {name: 'styles'}
    ];
};




