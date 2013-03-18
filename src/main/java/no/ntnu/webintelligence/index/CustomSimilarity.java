package no.ntnu.webintelligence.index;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.Similarity;

public class CustomSimilarity extends Similarity{

	@Override
	public float computeNorm(String arg0, FieldInvertState arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float coord(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float idf(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float queryNorm(float arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float sloppyFreq(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float tf(float arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
