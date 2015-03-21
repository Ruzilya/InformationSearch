package ru.kfu.search.postings;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
* Created by RuzilyaS on 13-Mar-15.
*/
public class LuceneIndexManager {

    private static Logger LOG = Logger.getLogger(LuceneIndexManager.class);

    public static String docsDirPath;
    public static String indexDirPath;

    private static IndexSearcher searcher;
    private static IndexReader reader;

    public LuceneIndexManager(String docsDirPath, String indexPath){
        this.docsDirPath=docsDirPath;
        this.indexDirPath=indexPath;
    }

    public LuceneIndexManager(){
    }

    public static final String FIELD_CONTENTS = "contents";
    public static final String FIELD_PATH = "path";
    public static final String ENCODING = "UTF-8";

    public void createIndex(){
        final File docDir=new File(docsDirPath);
        if (!docDir.exists() || !docDir.canRead()) {
            LOG.info("Document directory '" + docDir.getAbsolutePath() + "' does not exist or is not readable, please check the path");
            System.exit(1);
        }
        try {
            LOG.info("Indexing to directory '" + indexDirPath + "'...");
            Directory dir= FSDirectory.open(new File(indexDirPath));
            Analyzer analyzer=new StandardAnalyzer(Version.LUCENE_31);
            IndexWriterConfig iwc=new IndexWriterConfig(Version.LUCENE_31,analyzer);
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            IndexWriter writer=new IndexWriter(dir,iwc);
            indexDocs(writer,docDir);
            writer.close();
        }
        catch (IOException e) {
            LOG.error("IOException while creating index.", e);
        }
    }

    public IndexSearcher getSearcher() throws IOException {
        if(searcher!=null){
            return searcher;
        }
        reader = getReader();
        searcher = new IndexSearcher(reader);
        return searcher;
    }

    public IndexReader getReader() throws IOException {
        if(reader!=null){
            return reader;
        }
        File path = new File(indexDirPath);
        Directory index = FSDirectory.open(path);
        reader = DirectoryReader.open(index);
        return reader;
    }

    public Terms getTerms() throws IOException {
        Fields fields = MultiFields.getFields(getReader());
        Terms terms = fields.terms(FIELD_CONTENTS);
        return terms;
    }

    public List<Integer> getDocIds(String term)throws IOException{
        TermQuery query = new TermQuery(new Term(FIELD_CONTENTS, term));
        List<Integer> ids = new LinkedList<>();
        int count = getReader().getDocCount(FIELD_CONTENTS);

        SortField field = new SortField("docNum",
                SortField.Type.LONG, false);
        Sort sort = new Sort(field);

        TopDocs topdocs = getSearcher().search(query, count, sort);
        for ( ScoreDoc scoreDoc : topdocs.scoreDocs ) {
            ids.add(getDocId(scoreDoc.doc));
        }
        return ids;
    }

    //Because of general encoding ignores 0
    private int getDocId(int t){
//        return t+1;
        return t;
    }

    private void indexDocs(IndexWriter writer,File file) throws IOException {
        if (file.canRead()) {
            if (file.isDirectory()) {
                String[] files=file.list();
                if (files != null) {
                    for (int i=0; i < files.length; i++) {
                        indexDocs(writer,new File(file,files[i]));
                    }
                }
            }
            else {
                FileInputStream fis=new FileInputStream(file);
                Document doc=new Document();
                Field pathField=new Field(FIELD_PATH,file.getPath(),Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS);
                doc.add(pathField);
                doc.add(new Field(FIELD_CONTENTS,new BufferedReader(new InputStreamReader(fis,ENCODING))));
                writer.addDocument(doc);
                fis.close();
            }
        }
    }

    public static String getDocsDirPath() {
        return docsDirPath;
    }

    public static void setDocsDirPath(String docsDirPath) {
        LuceneIndexManager.docsDirPath = docsDirPath;
    }

    public static String getIndexDirPath() {
        return indexDirPath;
    }

    public static void setIndexDirPath(String indexDirPath) {
        LuceneIndexManager.indexDirPath = indexDirPath;
    }
}
