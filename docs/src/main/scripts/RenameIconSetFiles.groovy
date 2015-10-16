def iconsets = new File(project.basedir, 'documentation/features/ui/iconset')

iconsets.eachFile{
    def name = it.name
    if(name.contains('iconset')) {
        def iconsetId = it.name.replace('org.eclipse.smarthome.ui.iconset.', '')
        def simpleIconsetNameDir = new File(iconsets.path, iconsetId)
        it.renameTo(simpleIconsetNameDir)
        def readme = new File(simpleIconsetNameDir.path, 'README.md')
        if(readme.exists()) {
            println readme
            readme.renameTo(new File(simpleIconsetNameDir.path, 'readme.md'))
        }
    }
}