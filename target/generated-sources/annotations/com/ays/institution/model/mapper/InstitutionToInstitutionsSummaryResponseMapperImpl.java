package com.ays.institution.model.mapper;

import com.ays.institution.model.Institution;
import com.ays.institution.model.dto.response.InstitutionsSummaryResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-20T21:55:53+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19 (Oracle Corporation)"
)
public class InstitutionToInstitutionsSummaryResponseMapperImpl implements InstitutionToInstitutionsSummaryResponseMapper {

    @Override
    public InstitutionsSummaryResponse map(Institution source) {
        if ( source == null ) {
            return null;
        }

        InstitutionsSummaryResponse institutionsSummaryResponse = new InstitutionsSummaryResponse();

        institutionsSummaryResponse.setId( source.getId() );
        institutionsSummaryResponse.setName( source.getName() );

        return institutionsSummaryResponse;
    }

    @Override
    public List<InstitutionsSummaryResponse> map(Collection<Institution> sources) {
        if ( sources == null ) {
            return null;
        }

        List<InstitutionsSummaryResponse> list = new ArrayList<InstitutionsSummaryResponse>( sources.size() );
        for ( Institution institution : sources ) {
            list.add( map( institution ) );
        }

        return list;
    }
}
