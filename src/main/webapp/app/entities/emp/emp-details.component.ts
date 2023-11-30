import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import EmpService from './emp.service';
import { type IEmp } from '@/shared/model/emp.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'EmpDetails',
  setup() {
    const empService = inject('empService', () => new EmpService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const emp: Ref<IEmp> = ref({});

    const retrieveEmp = async empId => {
      try {
        const res = await empService().find(empId);
        emp.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.empId) {
      retrieveEmp(route.params.empId);
    }

    return {
      alertService,
      emp,

      previousState,
    };
  },
});
